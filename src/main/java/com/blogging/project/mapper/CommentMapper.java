package com.blogging.project.mapper;

import com.blogging.project.dto.CreateCommentDto;
import com.blogging.project.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {

    @Mapping(target = "id", ignore=true)
    @Mapping(target = "article", ignore=true)
    @Mapping(target = "user", ignore=true)
    @Mapping(target = "createdAt", ignore=true)
    @Mapping(target = "updatedAt", ignore=true)
    Comment toComment(CreateCommentDto commentDto);

}
