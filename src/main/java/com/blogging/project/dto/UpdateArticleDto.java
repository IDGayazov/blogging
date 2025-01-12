package com.blogging.project.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateArticleDto(
        String title,
        String content,
        LocalDate updatedAt,
        UUID categoryId
) {}
