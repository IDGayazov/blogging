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

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final String USERNAME_EXISTS_ERROR_TEMPLATE = "User with this username %s is already exists";
    private static final String EMAIL_EXISTS_ERROR_TEMPLATE = "User with this email %s is already exists";
    private static final String USERNAME_NOT_FOUND_ERROR_TEMPLATE = "Username %s not found";
    private static final String USER_NOT_FOUND_ERROR_TEMPLATE = "User id: %s not found";


    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ImageService imageService;

    public User create(User user){
        log.info("Creating user: {}", user.getUsername());

        checkUniqueUsernameAndEmail(user.getUsername(), user.getEmail());

        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    public User getByUsername(String username){
        log.info("Fetching user: {}", username);
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(format(USERNAME_NOT_FOUND_ERROR_TEMPLATE, username))
        );
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

        User user = getUserById(userId);

        checkSingleUserWithUsernameAndEmail(userId, userDto);

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
                () -> new EntityNotFoundException(format(USER_NOT_FOUND_ERROR_TEMPLATE, userId))
        );
    }

    private void checkUniqueUsernameAndEmail(String username, String email){
        if(userRepository.existsByUsername(username)){
            throw new UserAlreadyExistsException(format(USERNAME_EXISTS_ERROR_TEMPLATE, username));
        }
        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException(format(EMAIL_EXISTS_ERROR_TEMPLATE, email));
        }
    }

    private void checkSingleUserWithUsernameAndEmail(UUID userId, UpdatedUserDto userDto) {
        userRepository.findByUsername(userDto.username()).ifPresent(
                (u) -> {
                    if(!u.getId().equals(userId)){
                        throw new UserAlreadyExistsException(format(USERNAME_EXISTS_ERROR_TEMPLATE, userDto.username()));
                    }
                }
        );

        userRepository.findByEmail(userDto.email()).ifPresent(
                (u) -> {
                    if(!u.getId().equals(userId)){
                        throw new UserAlreadyExistsException(format(EMAIL_EXISTS_ERROR_TEMPLATE, userDto.email()));
                    }
                }
        );
    }

}
