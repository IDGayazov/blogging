package com.blogging.project.dto.comment;

import java.util.UUID;

public record CreateCommentDto (
        UUID userId,
        UUID articleId,
        String content
) {
}
