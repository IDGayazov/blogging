package com.blogging.project.service;

import com.blogging.project.entity.Article;
import com.blogging.project.entity.Like;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserService userService;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private LikeService likeService;

    @Test
    void testLikeArticleByUser() {
        UUID userId = UUID.randomUUID();
        UUID articleId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Article article = new Article();
        article.setId(articleId);

        when(userService.getUserById(userId)).thenReturn(user);
        when(articleService.getArticleById(articleId)).thenReturn(article);
        when(likeRepository.save(any(Like.class))).thenAnswer(ans -> ans.getArguments()[0]);

        likeService.likeArticleByUser(userId, articleId);

        ArgumentCaptor<Like> argumentCaptor = ArgumentCaptor.forClass(Like.class);
        verify(likeRepository).save(argumentCaptor.capture());

        Like like = argumentCaptor.getValue();

        assertEquals(userId, like.getUser().getId());
        assertEquals(articleId, like.getArticle().getId());
    }

    @Test
    void testUserEntityNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID articleId = UUID.randomUUID();
        when(userService.getUserById(userId)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> likeService.likeArticleByUser(userId, articleId));
    }

    @Test
    void testArticleEntityNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID articleId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(user);
        when(articleService.getArticleById(articleId)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> likeService.likeArticleByUser(userId, articleId));
    }
}