package com.blogging.project.service;

import com.blogging.project.dto.other.CreateCategoryDto;
import com.blogging.project.entity.Category;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.mapper.CategoryMapper;
import com.blogging.project.repository.CategoryRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        categoryService.getAllCategories();
        verify(categoryRepository).findAll();
    }

    @Nested
    class GetCategoryByIdTest{

        final UUID categoryId = UUID.randomUUID();
        final String categoryName = "Category name";

        @Test
        void testSuccess() {
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);

            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

            Category fetchedCategory = categoryService.getCategoryById(categoryId);

            assertEquals(categoryId, fetchedCategory.getId());
            assertEquals(categoryName, fetchedCategory.getName());
        }

        @Test
        void testCategoryEntityNotFoundException(){
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
        }
    }

    @Test
    void testCreateCategory() {
        final String categoryName = "Category name";
        final String categoryDescription = "Category description";

        CreateCategoryDto categoryDto = new CreateCategoryDto(
                categoryName,
                categoryDescription
        );

        Category mappedCategory = new Category();
        mappedCategory.setName(categoryName);
        mappedCategory.setDescription(categoryDescription);

        when(categoryMapper.toCategory(categoryDto)).thenReturn(mappedCategory);
        when(categoryRepository.save(any(Category.class))).thenAnswer(ans -> ans.getArguments()[0]);

        Category createdCategory = categoryService.createCategory(categoryDto);

        verify(categoryRepository).save(mappedCategory);

        assertEquals(categoryName, createdCategory.getName());
        assertEquals(categoryDescription, createdCategory.getDescription());
    }

    @Test
    void testDeleteCategoryById() {
        UUID categoryId = UUID.randomUUID();
        categoryService.deleteCategoryById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
}