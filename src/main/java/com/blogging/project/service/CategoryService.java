package com.blogging.project.service;


import com.blogging.project.dto.other.CreateCategoryDto;
import com.blogging.project.entity.Category;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.mapper.CategoryMapper;
import com.blogging.project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category with Id: %s not found";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<Category> getAllCategories(){
        log.info("Fetching all categories");
        return categoryRepository.findAll();
    }

    public Category getCategoryById(UUID categoryId){
        log.info("Fetching category with Id: {}", categoryId);
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format(CATEGORY_NOT_FOUND_MESSAGE, categoryId))
        );
    }

    public Category createCategory(CreateCategoryDto categoryDto){
        Category category = categoryMapper.toCategory(categoryDto);
        category.setCreatedAt(LocalDate.now());
        Category createdCategory = categoryRepository.save(category);
        log.info("Category with Id: {} was saved", category.getId());
        return createdCategory;
    }

    public void deleteCategoryById(UUID categoryId){
        categoryRepository.deleteById(categoryId);
        log.info("Category with Id: {} was deleted", categoryId);
    }

}
