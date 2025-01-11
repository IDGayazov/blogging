package com.blogging.project.dto;

import java.util.UUID;

public record CreateArticleDto(
        UUID userId,
        String title,
        String content,
        UUID categoryId
) {
}
