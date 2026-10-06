package com.creativepulse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/** Saves uploaded advertisement design files into the /uploads folder. */
@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload folder", e);
        }
    }

    public String store(MultipartFile file) {
        String original = Paths.get(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                .getFileName().toString();
        String stored = UUID.randomUUID() + "_" + original.replaceAll("\\s+", "_");
        try {
            Files.copy(file.getInputStream(), root.resolve(stored), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file " + original, e);
        }
        return stored;
    }

    public Path load(String storedFileName) {
        return root.resolve(storedFileName).normalize();
    }

    public void delete(String storedFileName) {
        if (storedFileName == null) return;
        try {
            Files.deleteIfExists(load(storedFileName));
        } catch (IOException ignored) { }
    }
}
