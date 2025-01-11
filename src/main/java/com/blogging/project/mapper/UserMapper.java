package com.blogging.project.mapper;

import com.blogging.project.dto.RegisterUserDto;
import com.blogging.project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "createdAt", ignore=true)
    @Mapping(target = "updatedAt", ignore=true)
    @Mapping(target = "id", ignore=true)
    @Mapping(target = "comments", ignore=true)
    @Mapping(target = "articles", ignore=true)
    @Mapping(target = "password", ignore=true)
    User toUser(RegisterUserDto registerUserDto);

}
