package com.blogging.project.controller;

import com.blogging.project.dto.article.CreateArticleDto;
import com.blogging.project.dto.article.UpdateArticleDto;
import com.blogging.project.entity.Article;
import com.blogging.project.entity.Comment;
import com.blogging.project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public List<Article> fetchAllArticles(){
        log.info("Receive request for fetching all articles");
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Article fetchArticleById(@PathVariable("id") UUID articleId){
        log.info("Receive request for fetch article by Id: {}", articleId);
        return articleService.getArticleById(articleId);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> fetchAllArticlesForComment(@PathVariable("id") UUID articleId){
        log.info("Receive request for fetching all comments");
        return articleService.getAllCommentsByArticleId(articleId);
    }

    @PostMapping(consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public Article createArticle(
        @RequestParam(required = true) String title,
        @RequestParam(required = true) String content,
        @RequestParam(required = false) UUID userId,
        @RequestParam(required = false) UUID categoryId,
        @RequestParam(required = false) MultipartFile image
    ){
        log.info("Receive request for saving article...");
        CreateArticleDto articleDto = new CreateArticleDto(
            title, content, image, userId, categoryId
        );
        return articleService.saveArticle(articleDto);
    }

    @PutMapping(path="/{id}", consumes={MediaType.MULTIPART_FORM_DATA_VALUE})
    public Article updateArticle(
        @PathVariable("id") UUID articleId, 
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String content,
        @RequestParam(required = false) UUID categoryId,
        @RequestParam(required = false) MultipartFile image
    ){
        log.info("Receive request for updating article with id: {}", articleId);
        UpdateArticleDto articleDto = new UpdateArticleDto(
            title, content, image, categoryId
        );
        return articleService.updateArticleById(articleId, articleDto);
    }

    @DeleteMapping("/{id}")
    public void removeArticleById(@PathVariable("id") UUID articleId){
        log.info("Receive request for remove article with Id: {}", articleId);
        articleService.removeArticleById(articleId);
    }
}
