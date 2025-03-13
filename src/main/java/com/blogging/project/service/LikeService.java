package com.blogging.project.service;

import com.blogging.project.entity.Article;
import com.blogging.project.entity.Like;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.repository.ArticleRepository;
import com.blogging.project.repository.LikeRepository;
import com.blogging.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final UserService userService;
    private final ArticleService articleService;

    public void likeArticleByUser(UUID userId, UUID articleId){
        User user = userService.getUserById(userId);
        Article article = articleService.getArticleById(articleId);

        Like like = new Like();
        like.setUser(user);
        like.setArticle(article);

        likeRepository.save(like);
        log.info("User {} liked article {}", userId, articleId);
    }

    public Set<Like> getAllLikesForUser(UUID userId){
        User user = userService.getUserById(userId);

        log.info("Fetching likes for user {}", userId);
        return user.getLikes();
    }

    public Set<Like> getAllLikesForArticle(UUID articleId){
        Article article = articleService.getArticleById(articleId);
        log.info("Fetching likes for article {}", articleId);
        return article.getLikes();
    }

    public void cancelLike(UUID userId, UUID articleId){
        log.info("Deleting like by {} for article {}", userId, articleId);
        likeRepository.deleteByUser_IdAndArticle_Id(userId, articleId);
    }

}
