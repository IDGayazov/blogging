package com.blogging.project.dto;


public record RegisterUserDto(
        String email,
        String name,
        String bio,
        String avatarUrl
) {
}
