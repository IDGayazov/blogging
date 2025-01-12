package com.blogging.project.dto;

import java.time.LocalDate;

public record UpdateCommentDto(
        String content,
        LocalDate updatedAt
) {
}
