package com.blogging.project.controller;

import com.blogging.project.dto.user.UpdatedUserDto;
import com.blogging.project.dto.user.UserDto;
import com.blogging.project.entity.Article;
import com.blogging.project.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") @NotNull UUID userId){
        return userService.getUserDtoById(userId);
    }

    @PutMapping(path="/{id}", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public UserDto updateUser(
            @PathVariable("id") UUID userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) MultipartFile avatarImage,
            @RequestParam(required = false) String bio
    ){
        UpdatedUserDto updatedUserDto = new UpdatedUserDto(
            email, username, firstname, lastname, avatarImage, bio
        );
        return userService.updateUser(userId, updatedUserDto);
    }

    @GetMapping("/{id}/articles")
    public List<Article> getAllArticlesForUser(@PathVariable("userId") @NotNull UUID userId){
        return userService.getAllArticles(userId);
    }

}
