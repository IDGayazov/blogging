package com.blogging.project.service;

import com.blogging.project.dto.CreateArticleDto;
import com.blogging.project.entity.Article;
import com.blogging.project.entity.Category;
import com.blogging.project.entity.User;
import com.blogging.project.mapper.ArticleMapper;
import com.blogging.project.repository.ArticleRepository;
import com.blogging.project.repository.CategoryRepository;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleMapper articleMapper;

    public void saveArticle(CreateArticleDto articleDto){
        Article article = articleMapper.toArticle(articleDto);

        User user = userRepository.getReferenceById(articleDto.userId());
        Category category = categoryRepository.getReferenceById(articleDto.categoryId());

        article.setUser(user);
        article.setCategory(category);
        article.setCreatedAt(LocalDate.now());
        article.setUpdatedAt(LocalDate.now());

        articleRepository.save(article);
        log.info("Save article with id: {}", article.getId());
    }
}
