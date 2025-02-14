package com.blogging.project.service;

import com.blogging.project.dto.user.UpdatedUserDto;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.UserAlreadyExistsException;
import com.blogging.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class CreateUserTest{
        private final String username = "username";
        private final String email = "email";
        private User user;

        @BeforeEach
        public void setUp(){
            user = User.builder()
                    .username(username)
                    .email(email)
                    .build();
        }

        @Test
        void testSuccess() {
            when(userRepository.existsByUsername(username)).thenReturn(false);
            when(userRepository.existsByEmail(email)).thenReturn(false);

            userService.create(user);

            verify(userRepository).save(user);
            assertEquals(LocalDate.now(), user.getCreatedAt());
            assertEquals(LocalDate.now(), user.getUpdatedAt());
        }

        @Test
        void testUserAlreadyExistsFailByUsername() {
            when(userRepository.existsByUsername(username)).thenReturn(true);
            assertThrows(UserAlreadyExistsException.class, () -> userService.create(user));
        }

        @Test
        void testUserAlreadyExistsFailEmail(){
            when(userRepository.existsByUsername(username)).thenReturn(false);
            when(userRepository.existsByEmail(email)).thenReturn(true);

            assertThrows(UserAlreadyExistsException.class, () -> userService.create(user));
        }
    }

    @Nested
    class GetByUsernameTest{
        final String username = "username";

        @Test
        void testSuccess() {
            User user = new User();
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            userService.getByUsername(username);

            verify(userRepository).findByUsername(username);
        }

        @Test
        void testUsernameNotFoundException(){
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
            assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(username));
        }
    }

    @Nested
    class UpdateUserTest{

        private final UUID userId = UUID.randomUUID();
        private UpdatedUserDto userDto;

        @BeforeEach
        public void setUp(){
            userDto = new UpdatedUserDto(
                 "email",
                 "username",
                 "firstname",
                 "lastname",
                 "avatarUrl",
                 "bio"
            );
        }

        @Test
        void testSuccess() {
            User user = new User();
            user.setId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(userDto.username())).thenReturn(Optional.of(user));

            User updatedUser = userService.updateUser(userId, userDto);

            verify(userRepository).save(user);

            assertEquals(userDto.username(), user.getUsername());
            assertEquals(userDto.bio(), user.getBio());
            assertEquals(userDto.avatarUrl(), user.getAvatarUrl());
            assertEquals(userDto.firstname(), user.getFirstname());
            assertEquals(userDto.lastname(), user.getLastname());
            assertEquals(userDto.email(), user.getEmail());
            assertEquals(LocalDate.now(), user.getUpdatedAt());
        }

        @Test
        void testUsernameAlreadyExistsFail(){
            User user = new User();
            user.setId(userId);

            User anotherUser = new User();
            anotherUser.setId(UUID.randomUUID());

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(userDto.username())).thenReturn(Optional.of(anotherUser));

            assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(userId, userDto));
        }

        @Test
        void testEmailAlreadyExistsFail(){
            User user = new User();
            user.setId(userId);

            User anotherUser = new User();
            anotherUser.setId(UUID.randomUUID());

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(userDto.username())).thenReturn(Optional.of(user));
            when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(anotherUser));

            assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(userId, userDto));
        }
    }
}