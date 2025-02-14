package com.blogging.project.service;

import com.blogging.project.dto.user.SignInRequestDto;
import com.blogging.project.dto.user.SignUpRequestDto;
import com.blogging.project.entity.Role;
import com.blogging.project.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testSignIn(){
        SignInRequestDto requestDto = new SignInRequestDto(
                "username",
                "password"
        );

        User user = new User();
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);

        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(requestDto.username())).thenReturn(user);

        authenticationService.signIn(requestDto);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.username(),
                requestDto.password()
        ));
        verify(jwtService).generateToken(user);
    }

    @Test
    void testSignUp() {
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "username",
                    "email",
                "firstname",
                "lastname",
                "password"
        );

        when(passwordEncoder.encode(any())).thenAnswer(ans -> ans.getArguments()[0]);

        authenticationService.signUp(requestDto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(jwtService).generateToken(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(requestDto.email(), capturedUser.getEmail());
        assertEquals(requestDto.username(), capturedUser.getUsername());
        assertEquals(requestDto.firstname(), capturedUser.getFirstname());
        assertEquals(requestDto.lastname(), capturedUser.getLastname());
        assertEquals(requestDto.password(), capturedUser.getPassword());
        assertEquals(LocalDate.now(), capturedUser.getCreatedAt());
        assertEquals(LocalDate.now(), capturedUser.getUpdatedAt());
        assertEquals(Role.ROLE_USER, capturedUser.getRole());
    }
}