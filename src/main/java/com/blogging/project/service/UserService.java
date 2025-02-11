package com.blogging.project.service;

import com.blogging.project.dto.user.UpdatedUserDto;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.exceptions.UserAlreadyExistsException;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("User with this username is already exists");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("User with this email is already exists");
        }
        return userRepository.save(user);
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public UpdatedUserDto updateUser(UUID userId, UpdatedUserDto userDto){
        log.info("Request for updating user: {}", userDto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.findByUsername(userDto.username()).ifPresent(
            (u) -> {
                if(!u.getId().equals(userId)){
                    throw new UserAlreadyExistsException("Username is already exists");
                }
            }
        );

        user.setEmail(userDto.email());
        user.setUpdatedAt(LocalDate.now());
        user.setBio(userDto.bio());
        user.setAvatarUrl(userDto.avatarUrl());
        user.setFirstname(userDto.firstname());
        user.setLastname(userDto.lastname());
        user.setUsername(userDto.username());

        UpdatedUserDto updatedUserDto = new UpdatedUserDto(
                userDto.email(),
                userDto.username(),
                userDto.firstname(),
                userDto.lastname(),
                userDto.avatarUrl(),
                userDto.bio(),
                user.getUpdatedAt()
        );

        userRepository.save(user);
        log.info("Update user with id: {}", user.getId());

        return updatedUserDto;
    }

}
