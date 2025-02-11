package com.blogging.project.controller;

import com.blogging.project.dto.user.JwtAuthenticationResponse;
import com.blogging.project.dto.user.SignInRequestDto;
import com.blogging.project.dto.user.SignUpRequestDto;
import com.blogging.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequestDto signInRequestDto){
        return authenticationService.signIn(signInRequestDto);
    }

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return authenticationService.signUp(signUpRequestDto);
    }

}
