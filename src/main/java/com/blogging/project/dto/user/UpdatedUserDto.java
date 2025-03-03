package com.blogging.project.dto.user;

import jakarta.validation.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

public record UpdatedUserDto(
        @Email(message="Email is not valid", regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
        String email,
        String username,
        String firstname,
        String lastname,
        MultipartFile avatarImage,
        String bio
) {
}
