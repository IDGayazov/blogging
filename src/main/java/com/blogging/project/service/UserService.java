package com.blogging.project.service;

import com.blogging.project.dto.RegisterUserDto;
import com.blogging.project.mapper.UserMapper;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void registerUser(final RegisterUserDto userDto){
        // TODO
    }

}
