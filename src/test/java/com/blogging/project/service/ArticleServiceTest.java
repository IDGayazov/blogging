package com.blogging.project.service;

import com.blogging.project.dto.article.CreateArticleDto;
import com.blogging.project.dto.article.UpdateArticleDto;
import com.blogging.project.entity.Article;
import com.blogging.project.entity.Category;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.mapper.ArticleMapper;
import com.blogging.project.service.ImageService;
import com.blogging.project.repository.ArticleRepository;
import com.blogging.project.repository.CategoryRepository;
import com.blogging.project.repository.CommentRepository;
import com.blogging.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogging.project.mock.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ArticleService articleService;

    @Nested
    class SaveArticleTest{
        final UUID userId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final String articleTitle = "Article title";
        final String imageUrl = "image url";
        final String articleContent = "Article content";
        
        CreateArticleDto articleDto;
        Article mappedArticle;

        @BeforeEach()
        public void setUp(){
            byte[] content = "Hello, World!".getBytes();
            articleDto = new CreateArticleDto(
                    articleTitle,
                    articleContent,
                    new MockMultipartFile(
                      "file",
                      "file.txt",
                      "file",
                       content
                    ),
                    userId,
                    categoryId
            );

            mappedArticle = new Article();

            mappedArticle.setTitle(articleTitle);
            mappedArticle.setContent(articleContent);

            when(articleMapper.toArticle(articleDto)).thenReturn(mappedArticle);
        }

        @Test
        void testSuccess() {
            when(imageService.handleFileUpload(articleDto.image())).thenReturn(imageUrl);
            when(articleRepository.save(any(Article.class)))
                    .thenAnswer((argument) -> argument.getArguments()[0]);
            when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));

            Article article = articleService.saveArticle(articleDto);

            verify(articleRepository).save(mappedArticle);

            assertEquals(articleContent, article.getContent());
            assertEquals(articleTitle, article.getTitle());
            assertEquals(imageUrl, article.getAvatarUrl());
            assertEquals(LocalDate.now(), article.getCreatedAt());
            assertEquals(LocalDate.now(), article.getUpdatedAt());
        }

        @Test
        void testThrowUserEntityNotFoundException(){
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> articleService.saveArticle(articleDto));
        }

        @Test
        void testThrowCategoryEntityNotFoundException(){
            when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> articleService.saveArticle(articleDto));
        }
    }

    @Nested
    class GetArticleByIdTest{
        @Test
        void testSuccess() {
            UUID articleId = UUID.randomUUID();

            Article article = new Article();
            article.setId(articleId);

            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

            Article fetchedArticle = articleService.getArticleById(articleId);
            assertEquals(articleId, fetchedArticle.getId());
        }

        @Test
        void testThrowEntityNotFoundException() {
            UUID articleId = UUID.randomUUID();
            when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> articleService.getArticleById(articleId));
        }
    }

    @Test
    void testGetAllArticles() {
        List<Article> articlesList = articleService.getAllArticles();
        verify(articleRepository).findAll();
    }

    @Test
    void testRemoveArticleById() {
        UUID articleId = UUID.randomUUID();
        articleService.removeArticleById(articleId);
        verify(articleRepository).deleteById(articleId);
    }

    @Test
    void testGetAllCommentsForArticle(){
        UUID articleId = UUID.randomUUID();
        articleService.getAllCommentsByArticleId(articleId);
        verify(commentRepository).findAllByArticle_Id(articleId);
    }

    @Nested
    class UpdateArticleByIdTest{
        final UUID articleId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final String articleTitle = "Article title";
        final String articleContent = "Article content";
        final String imageUrl = "image url";

        UpdateArticleDto articleDto;
        Article article;
        Category category;

        @BeforeEach
        public void setUp(){
            byte[] content = "Hello, World!".getBytes();
            articleDto = new UpdateArticleDto(
                    articleTitle,
                    articleContent,
                    new MockMultipartFile(
                      "file",
                      "file.txt",
                      "file",
                       content
                    ),
                    categoryId
            );

            article = new Article();
            category = new Category();
        }

        @Test
        void testSuccess() {
            when(imageService.handleFileUpload(articleDto.image())).thenReturn(imageUrl);
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
            when(articleRepository.save(any(Article.class))).thenAnswer((args) -> args.getArguments()[0]);

            Article updatedArticle = articleService.updateArticleById(articleId, articleDto);

            verify(articleRepository).save(updatedArticle);

            assertEquals(articleTitle, updatedArticle.getTitle());
            assertEquals(articleContent, updatedArticle.getContent());
            assertEquals(imageUrl, updatedArticle.getAvatarUrl());
            assertEquals(LocalDate.now(), updatedArticle.getUpdatedAt());
        }

        @Test
        void testThrowArticleEntityNotFoundException() {
            when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> articleService.updateArticleById(articleId, articleDto));
        }

        @Test
        void testThrowCategoryEntityNotFoundException() {
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
            when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> articleService.updateArticleById(articleId, articleDto));
        }
    }
}