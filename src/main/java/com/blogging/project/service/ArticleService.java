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

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final ArticleMapper articleMapper;

    public Article saveArticle(CreateArticleDto articleDto){
        Article article = articleMapper.toArticle(articleDto);

        Optional<User> userOpt = userRepository.findById(articleDto.userId());
        userOpt.ifPresentOrElse(article::setUser, () -> {
            throw new EntityNotFoundException(String.format("User with Id: %s  not found", articleDto.userId()));
        });

        Optional<Category> categoryOpt = categoryRepository.findById(articleDto.categoryId());
        categoryOpt.ifPresentOrElse(article::setCategory, () -> {
            throw new EntityNotFoundException(String.format("Category with Id: %s  not found", articleDto.categoryId()));
        });

        article.setCreatedAt(LocalDate.now());
        article.setUpdatedAt(LocalDate.now());

        articleRepository.save(article);
        log.info("Save article with id: {}", article.getId());

        return article;
    }

    public Article getArticleById(UUID articleId){
        log.info("Fetching article with id: {}", articleId);
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Article with Id: %s not found", articleId)));
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
        Optional<Article> currentArticleOpt = articleRepository.findById(articleId);
        Article article = currentArticleOpt.orElseThrow(
                () -> new EntityNotFoundException(String.format("Article with Id: %s not found", articleId))
        );

        Optional<Category> categoryOpt = categoryRepository.findById(articleDto.categoryId());
        categoryOpt.ifPresentOrElse(article::setCategory,
                () -> {
                    throw new EntityNotFoundException(String.format("Category with Id: %s  not found", articleDto.categoryId()));
                }
        );

        article.setTitle(articleDto.title());
        article.setContent(articleDto.content());
        article.setAvatarUrl(articleDto.avatarUrl());
        article.setUpdatedAt(LocalDate.now());

        Article updatedArticle = articleRepository.save(article);
        log.info("Update article with id: {}", articleId);
        return updatedArticle;
    }
}
