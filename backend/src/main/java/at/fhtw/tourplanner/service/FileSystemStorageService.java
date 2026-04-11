package at.fhtw.tourplanner.service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import at.fhtw.tourplanner.exeception.StorageException;
import at.fhtw.tourplanner.exeception.StorageFileNotFoundException;
import at.fhtw.tourplanner.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, UUID accountUuid, UUID tourUuid) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path folderPath = Files.createDirectories(Path.of(this.rootLocation.toString(), accountUuid.toString()));
            File dest = new File(folderPath + "/" + tourUuid.toString() + ".png");

            Path destinationFile = folderPath.resolve(
                            Paths.get(dest.getAbsolutePath()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(folderPath.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (OutputStream os = new FileOutputStream(dest)) {
                os.write(file.getBytes());
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll(UUID accountUuid) {
        try {
            Path filePath = Files.createDirectories(Path.of(this.rootLocation.toString(), accountUuid.toString()));
            return Files.walk(filePath, 1)
                    .filter(path -> !path.equals(filePath))
                    .map(filePath::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename, UUID accountUuid) {
        try {
            return Files.createDirectories(Path.of(this.rootLocation.toString(), accountUuid.toString())).resolve(filename);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename, UUID accountUuid) {
        try {
            Path file = load(filename, accountUuid);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename, UUID accountUuid) {
        try {
            Path folderPath = Files.createDirectories(Path.of(this.rootLocation.toString(), accountUuid.toString()));
            Files.delete(folderPath.resolve(filename));
        } catch (IOException e) {
            throw new StorageException("Failed to delete stored file", e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
