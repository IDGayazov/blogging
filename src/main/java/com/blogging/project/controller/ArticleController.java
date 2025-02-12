package com.blogging.project.controller;

import com.blogging.project.dto.article.CreateArticleDto;
import com.blogging.project.dto.article.UpdateArticleDto;
import com.blogging.project.entity.Article;
import com.blogging.project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public void createArticle(@RequestBody CreateArticleDto articleDto){
        log.info("Receive request for saving article...");
        articleService.saveArticle(articleDto);
    }

    @PutMapping("/{id}")
    public void updateArticle(@PathVariable("id") UUID articleId, @RequestBody UpdateArticleDto updateArticleDto){
        log.info("Receive request for updating article with id: {}", articleId);
        articleService.updateArticleById(articleId, updateArticleDto);
    }

    @DeleteMapping("/{id}")
    public void removeArticleById(@PathVariable("id") UUID articleId){
        log.info("Receive request for remove article with Id: {}", articleId);
        articleService.removeArticleById(articleId);
    }
}
