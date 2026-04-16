package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.service.FileSystemStorageService;
import at.fhtw.tourplanner.exeception.StorageFileNotFoundException;
import at.fhtw.tourplanner.util.PrincipalCheckUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/files")
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class FileUploadController {
    private final FileSystemStorageService fileSystemStorageService;

    @GetMapping("/")
    public String listUploadedFiles(Model model, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        model.addAttribute("files", fileSystemStorageService.loadAll(account.getUuid()).map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        Resource file = fileSystemStorageService.loadAsResource(filename, account.getUuid());

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(file);
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String uuid, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        fileSystemStorageService.store(file, account.getUuid(), UUID.fromString(uuid));

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<String> deleteFiles(@PathVariable String filename, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        fileSystemStorageService.delete(filename, account.getUuid());
        return ResponseEntity.ok(null);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
