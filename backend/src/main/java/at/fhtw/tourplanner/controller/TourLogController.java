package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.dto.TourLogDto;
import at.fhtw.tourplanner.dto.TourLogUpdateDto;
import at.fhtw.tourplanner.service.TourLogService;
import at.fhtw.tourplanner.util.PrincipalCheckUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class TourLogController {
    private final TourLogService tourLogService;

    @GetMapping("/tours/{uuid}/logs")
    public ResponseEntity<List<TourLogDto>> getAllTourLogs(@PathVariable String uuid, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourLogService.getAllTourLogsOfTour(UUID.fromString(uuid), account);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/logs/{uuid}")
    public ResponseEntity<TourLogDto> getTourLog(@PathVariable String uuid, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourLogService.getTourLogByUuid(UUID.fromString(uuid), account);

        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/logs/{uuid}/search/{searchTerm}")
    public ResponseEntity<Page<TourLogDto>> getTourLog(@PathVariable String searchTerm, @PathVariable String uuid, Pageable pageable, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourLogService.searchTourLog(searchTerm, UUID.fromString(uuid), pageable, account);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/logs")
    public ResponseEntity<Optional<TourLogDto>> addTourLog(@Valid @RequestBody TourLogDto tourLogDto, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourLogService.addTourLog(tourLogDto, account);

        return result.map(_ -> ResponseEntity.created(URI.create("/api/v1/logs")).body(result))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/logs")
    public ResponseEntity<TourLogDto> updateTourLog(@Valid @RequestBody TourLogUpdateDto tourLogUpdateDto, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourLogService.updateTourLog(tourLogUpdateDto, account);

        return result.map(_ -> ResponseEntity.of(result))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/logs/{uuid}")
    public ResponseEntity<String> deleteTourLog(@PathVariable String uuid, Authentication authentication) {
        var account = PrincipalCheckUtil.getPrincipal(authentication);
        var result = tourLogService.deleteTour(UUID.fromString(uuid), account);
        if (result.isPresent()) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
