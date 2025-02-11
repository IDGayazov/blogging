package com.blogging.project.dto.user;

import com.blogging.project.validation.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto (
        @NotBlank(message="Username can't be empty")
        String username,

        @NotBlank(message="Email can't be empty")
        @Email(message="Email is not valid", regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        String email,

        @NotBlank(message="Firstname can't be empty")
        String firstname,

        @NotBlank(message="Lastname can't be empty")
        String lastname,

        @NotBlank(message="Password can't be empty")
        @PasswordValidation(message="Password must consist at least 6 characters")
        String password
){}
