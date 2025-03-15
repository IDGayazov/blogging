package com.blogging.project.mapper;

import com.blogging.project.dto.user.UserDto;
import com.blogging.project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target="articles", ignore=true)
    UserDto toUserDto(User user);
}
