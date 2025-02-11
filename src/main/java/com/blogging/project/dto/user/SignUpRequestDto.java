package com.blogging.project.dto.user;

//TODO: добавить валидацию

public record SignUpRequestDto (
        String username,
        String email,
        String firstname,
        String lastname,
        String password
){}
