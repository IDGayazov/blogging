package com.blogging.project.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserDto{
        UUID id;
        String username;
        String firstname;
        String lastname;
        String email;
        String bio;
        String avatarUrl;
        LocalDate createdAt;
        LocalDate updatedAt;
        List<UUID> articles;
}
