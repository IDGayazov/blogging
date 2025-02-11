package com.blogging.project.service;

import com.blogging.project.entity.User;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("User with this username is already exists");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("User with this email is already exists");
        }
        return userRepository.save(user);
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

}
