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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user){
        log.info("Creating user: {}", user.getUsername());
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("User with this username is already exists");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("User with this email is already exists");
        }
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        return userRepository.save(user);
    }

    public User getByUsername(String username){
        log.info("Fetching user: {}", username);
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

    public User updateUser(UUID userId, UpdatedUserDto userDto){
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

        userRepository.findByEmail(userDto.email()).ifPresent(
                (u) -> {
                    if(!u.getId().equals(userId)){
                        throw new UserAlreadyExistsException("Email is already exists");
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

        userRepository.save(user);
        log.info("Update user with id: {}", user.getId());

        return user;
    }

}
