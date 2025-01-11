package com.blogging.project.controller;

import com.blogging.project.dto.CreateArticleDto;
import com.blogging.project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public void createArticle(@RequestBody CreateArticleDto articleDto){
        log.info("Receive request for saving article...");
        articleService.saveArticle(articleDto);
    }

}
