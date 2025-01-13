package com.blogging.project.controller;

import com.blogging.project.dto.CreateCategoryDto;
import com.blogging.project.entity.Category;
import com.blogging.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories(){
        log.info("Request for fetching all categories");
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(UUID categoryId){
        log.info("Request for fetching category by Id: {}", categoryId);
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public void createCategory(@RequestBody CreateCategoryDto categoryDto){
        log.info("Request for saving categories");
        categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("/id") UUID categoryId){
        log.info("Request for delete category with Id: {}", categoryId);
        categoryService.deleteCategoryById(categoryId);
    }

}
