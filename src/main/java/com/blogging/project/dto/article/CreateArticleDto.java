package com.blogging.project.dto.article;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile; 

public record CreateArticleDto(
        @NotBlank(message="Title can't be empty")
        String title,

        @NotBlank(message="Content can't be empty")
        String content,

        MultipartFile image,
        UUID userId,
        UUID categoryId
) {
}
