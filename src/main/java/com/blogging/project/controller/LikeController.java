package com.blogging.project.controller;

import com.blogging.project.entity.Like;
import com.blogging.project.service.LikeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/articles/{articleId}")
    public Set<Like> getLikesForArticle(@PathVariable("articleId") @NotNull UUID articleId){
        return likeService.getAllLikesForArticle(articleId);
    }

    @GetMapping("/users/{userId}")
    public Set<Like> getLikesForUser(@PathVariable("userId") @NotNull UUID userId){
        return likeService.getAllLikesForUser(userId);
    }

    @PostMapping("/{userId}/{articleId}")
    public ResponseEntity<Void> like(@PathVariable("userId") @NotNull UUID userId,
            @PathVariable("articleId") @NotNull UUID articleId) {
        likeService.likeArticleByUser(userId, articleId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{articleId}")
    public ResponseEntity<Void> unlike(@PathVariable("userId") @NotNull UUID userId,
            @PathVariable("articleId") @NotNull UUID articleId) {
        likeService.cancelLike(userId, articleId);
        return ResponseEntity.ok().build();
    }

}
