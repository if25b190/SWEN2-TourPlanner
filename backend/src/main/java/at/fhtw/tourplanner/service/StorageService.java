package at.fhtw.tourplanner.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    void store(MultipartFile file, UUID accountUuid, UUID tourUuid);

    Stream<Path> loadAll(UUID accountUuid);

    Path load(String filename, UUID accountUuid);

    Resource loadAsResource(String filename, UUID accountUuid);

    void delete(String filename, UUID accountUuid);
}