package com.blogging.project.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatedUserDto(
        @Email(message="Email is not valid", regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        String email,
        @NotBlank(message="Username can't be empty")
        String username,
        @NotBlank(message="Firstname can't be empty")
        String firstname,
        @NotBlank(message="Lastname can't be empty")
        String lastname,
        String avatarUrl,
        String bio,
        LocalDate updatedAt
) {
}
