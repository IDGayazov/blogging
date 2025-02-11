package com.blogging.project.dto.user;

//TODO: валидация

public record SignInRequestDto(
        String username,
        String password
) {
}
