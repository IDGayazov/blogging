package com.blogging.project.dto.article;

import java.util.UUID;

public record UpdateArticleDto(
        String title,
        String content,
        String avatarUrl,
        UUID categoryId
) {}
