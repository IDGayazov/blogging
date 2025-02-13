package com.blogging.project.mapper;

import com.blogging.project.dto.article.CreateArticleDto;
import com.blogging.project.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "createArticleDto.title")
    @Mapping(target = "content", source = "createArticleDto.content")
    @Mapping(target = "avatarUrl", source = "createArticleDto.avatarUrl")
    Article toArticle(CreateArticleDto createArticleDto);
}
