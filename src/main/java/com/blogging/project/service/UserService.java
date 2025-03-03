package com.blogging.project.service;

import com.blogging.project.dto.user.UpdatedUserDto;
import com.blogging.project.entity.Article;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.exceptions.UserAlreadyExistsException;
import com.blogging.project.repository.ArticleRepository;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ImageService imageService;

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

        checkUniqueUsernameAndEmail(userId, userDto);

        Optional.ofNullable(userDto.avatarImage())
                .ifPresent(avatarImage -> {
                    String filePath = imageService.handleFileUpload(userDto.avatarImage());
                    user.setAvatarUrl(filePath);
                });

        Optional.ofNullable(userDto.email()).filter(email -> !email.isEmpty()).ifPresent(user::setEmail);
        Optional.ofNullable(userDto.username()).filter(username -> !username.isEmpty()).ifPresent(user::setUsername);
        Optional.ofNullable(userDto.firstname()).filter(firstname -> !firstname.isEmpty()).ifPresent(user::setFirstname);
        Optional.ofNullable(userDto.lastname()).filter(lastname -> !lastname.isEmpty()).ifPresent(user::setLastname);
        Optional.ofNullable(userDto.bio()).filter(bio -> !bio.isEmpty()).ifPresent(user::setBio);

        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);
        log.info("Update user with id: {}", user.getId());
        return user;
    }

    public List<Article> getAllArticles(UUID userId){
        log.info("Request for fetching articles for user by id {}", userId);
        return articleRepository.findAllByUser_Id(userId);
    }

    public User getUserById(UUID userId){
        log.info("Request for fetching user by id");
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User id: %s not found", userId))
        );
    }

    private void checkUniqueUsernameAndEmail(UUID userId, UpdatedUserDto userDto) {
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
    }

}
