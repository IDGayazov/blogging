package com.blogging.project.exceptions;

public record AppError(
        int statusCode,
        String info
) {}
