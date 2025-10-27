package com.zheheng.InventoryManagementSpringboot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
@Slf4j
public class GetImage {

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-images/";

    /**
     * Fetch product image by file name
     * Example: GET /api/images/9211d29b-8e36-455e-82c6-0af17b690f97_StallSync.png
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(IMAGE_DIRECTORY).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                log.warn("Image not found: {}", filename);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            log.error("Error while loading image: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error while retrieving image: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
