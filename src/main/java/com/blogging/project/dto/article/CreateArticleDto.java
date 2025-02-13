package com.blogging.project.dto.article;

import java.util.UUID;

public record CreateArticleDto(
        UUID userId,
        String title,
        String content,
        String avatarUrl,
        UUID categoryId
) {
}
