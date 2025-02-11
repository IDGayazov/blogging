package com.blogging.project.dto.comment;

import java.time.LocalDate;

public record UpdateCommentDto(
        String content,
        LocalDate updatedAt
) {
}
