package com.blogging.project.mapper;

import com.blogging.project.dto.other.CreateCategoryDto;
import com.blogging.project.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target="id", ignore=true)
    @Mapping(target="createdAt", ignore=true)
    @Mapping(target="articles", ignore=true)
    Category toCategory(CreateCategoryDto categoryDto);

}
