package com.blogging.project.dto.user;

import com.blogging.project.validation.PasswordValidation;
import jakarta.validation.constraints.NotBlank;

public record SignInRequestDto(
        @NotBlank(message="Username can't be empty")
        String username,

        @NotBlank(message="Password can't be empty")
        @PasswordValidation(message="Password must consist at least 6 characters")
        String password
) {}
