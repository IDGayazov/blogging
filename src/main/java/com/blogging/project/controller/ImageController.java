package com.blogging.project.controller;

import com.blogging.project.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/images/")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService  imageService;

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable("fileName") String fileName) {
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
        MediaType contentType = mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(imageService.handleFileFetch(fileName));
    }
}
