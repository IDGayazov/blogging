package com.blogging.project.controller;

import com.blogging.project.dto.user.UpdatedUserDto;
import com.blogging.project.entity.User;
import com.blogging.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") UUID userId, @RequestBody @Valid UpdatedUserDto updatedUserDto){
        return userService.updateUser(userId, updatedUserDto);
    }

}
