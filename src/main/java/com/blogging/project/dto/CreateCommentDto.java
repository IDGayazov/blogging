package com.blogging.project.dto;

import java.util.UUID;

public record CreateCommentDto (
        UUID userId,
        UUID articleId,
        String content
) {
}
