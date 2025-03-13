package com.blogging.project.service;

import com.blogging.project.dto.article.CreateArticleDto;
import com.blogging.project.dto.article.UpdateArticleDto;
import com.blogging.project.entity.Article;
import com.blogging.project.entity.Category;
import com.blogging.project.entity.Comment;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.mapper.ArticleMapper;
import com.blogging.project.repository.ArticleRepository;
import com.blogging.project.repository.CategoryRepository;
import com.blogging.project.repository.CommentRepository;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private static final String CATEGORY_NOT_FOUND_MESSAGE_TEMPLATE = "Category with Id: %s not found";
    private static final String ARTICLE_NOT_FOUND_MESSAGE_TEMPLATE = "Article with Id: %s not found";
    private static final String USER_NOT_FOUND_MESSAGE_TEMPLATE = "User with Id: %s not found";

    private final ImageService imageService;

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final ArticleMapper articleMapper;

    public Article saveArticle(CreateArticleDto articleDto){
        Article article = getNewArticleFromCreateArticleDto(articleDto);
        articleRepository.save(article);
        log.info("Save article with id: {}", article.getId());
        return article;
    }

    public Article getArticleById(UUID articleId){
        log.info("Fetching article with id: {}", articleId);
        return getArticleByIdOrElseThrowException(articleId);
    }

    public List<Comment> getAllCommentsByArticleId(UUID articleId){
        log.info("Fetching comments with articleId: {}", articleId);
        return commentRepository.findAllByArticle_Id(articleId);
    }

    public List<Article> getAllArticles(){
        log.info("Fetching articles");
        return articleRepository.findAll();
    }

    public void removeArticleById(UUID articleId){
        articleRepository.deleteById(articleId);
        log.info("Delete article by Id: {}", articleId);
    }

    public Article updateArticleById(UUID articleId, UpdateArticleDto articleDto){
        Article article = getArticleByIdOrElseThrowException(articleId);

        updateArticleFieldsFromUpdateArticleDto(article, articleDto);
        
        articleRepository.save(article);

        log.info("Update article with id: {}", articleId);

        return article;
    }

    private Category getCategoryByIdOrElseThrowException(UUID categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException(format(CATEGORY_NOT_FOUND_MESSAGE_TEMPLATE, categoryId))
        );
    }

    private User getUserByIdOrElseThrowException(UUID userId){
        return userRepository.findById(userId).orElseThrow(
            () -> new EntityNotFoundException(format(USER_NOT_FOUND_MESSAGE_TEMPLATE, userId))
        );
    }

    private Article getArticleByIdOrElseThrowException(UUID articleId){
        return articleRepository.findById(articleId).orElseThrow(
            () -> new EntityNotFoundException(format(ARTICLE_NOT_FOUND_MESSAGE_TEMPLATE, articleId))
        );
    }

    private Article getNewArticleFromCreateArticleDto(CreateArticleDto articleDto){
        Article article = articleMapper.toArticle(articleDto);

        Optional.ofNullable(articleDto.userId()).ifPresent(userId -> {
            User user = getUserByIdOrElseThrowException(userId);
            article.setUser(user);
        });

        Optional.ofNullable(articleDto.categoryId()).ifPresent(categoryId -> {
            Category category = getCategoryByIdOrElseThrowException(categoryId);
            article.setCategory(category);
        });

        Optional.ofNullable(articleDto.image()).ifPresent(image -> {
            article.setAvatarUrl(imageService.handleFileUpload(image));
        });

        article.setCreatedAt(LocalDate.now());
        article.setUpdatedAt(LocalDate.now());

        return article;
    }

    private void updateArticleFieldsFromUpdateArticleDto(Article article, UpdateArticleDto articleDto){
        Optional.ofNullable(articleDto.title()).ifPresent(article::setTitle);

        Optional.ofNullable(articleDto.content()).ifPresent(article::setContent);

        Optional.ofNullable(articleDto.categoryId()).ifPresent(categoryId -> {
            Category category = getCategoryByIdOrElseThrowException(categoryId);
            article.setCategory(category);
        });

        Optional.ofNullable(articleDto.image()).ifPresent(image -> {
            article.setAvatarUrl(imageService.handleFileUpload(image));
        });

        article.setUpdatedAt(LocalDate.now());
    }

}
