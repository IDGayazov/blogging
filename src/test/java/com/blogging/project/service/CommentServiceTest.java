package com.blogging.project.service;

import com.blogging.project.dto.comment.CreateCommentDto;
import com.blogging.project.dto.comment.UpdateCommentDto;
import com.blogging.project.entity.Article;
import com.blogging.project.entity.Comment;
import com.blogging.project.entity.User;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.mapper.CommentMapper;
import com.blogging.project.repository.ArticleRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @Nested
    class GetCommentByIdTest{

        final UUID commentId = UUID.randomUUID();

        @Test
        void testSuccess() {
            Comment comment = new Comment();
            comment.setId(commentId);

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

            Comment fetchedComment = commentService.getCommentById(commentId);
            assertEquals(commentId, fetchedComment.getId());
        }

        @Test
        void testCommentEntityNotFoundException(){
            when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(commentId));
        }
    }

    @Test
    void testGetAllComments() {
        commentService.getAllComments();
        verify(commentRepository).findAll();
    }

    @Test
    void testDeleteCommentById() {
        UUID commentId = UUID.randomUUID();
        commentService.deleteCommentById(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @Nested
    class SaveCommentTest{

        private final UUID userId = UUID.randomUUID();
        private final UUID articleId = UUID.randomUUID();
        private final String commentContent = "Comment content";
        private CreateCommentDto commentDto;
        private Comment mappedComment;

        @BeforeEach
        public void setUp(){
            commentDto =  new CreateCommentDto(
                    userId,
                    articleId,
                    commentContent
            );

            mappedComment = new Comment();
            mappedComment.setContent(commentContent);

            when(commentMapper.toComment(commentDto)).thenReturn(mappedComment);
        }

        @Test
        void testSaveComment() {
            User user = new User();
            user.setId(userId);

            Article article = new Article();
            article.setId(articleId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
            when(commentRepository.save(any(Comment.class))).thenAnswer(ans -> ans.getArguments()[0]);

            Comment createdComment = commentService.saveComment(commentDto);

            assertEquals(commentContent, createdComment.getContent());
            assertEquals(LocalDate.now(), createdComment.getCreatedAt());
            assertEquals(LocalDate.now(), createdComment.getUpdatedAt());
            assertEquals(userId, createdComment.getUser().getId());
            assertEquals(articleId, createdComment.getArticle().getId());
        }

        @Test
        void testUserEntityNotFoundException() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> commentService.saveComment(commentDto));
        }

        @Test
        void testArticleEntityNotFoundException() {
            User user = new User();
            user.setId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> commentService.saveComment(commentDto));
        }
    }

    @Nested
    class UpdateCommentByIdTest{
        private final UUID commentId = UUID.randomUUID();
        private final String commentContent = "Comment content";
        private UpdateCommentDto commentDto;

        @BeforeEach
        public void setUp(){
            commentDto = new UpdateCommentDto(commentContent);
        }

        @Test
        void testSuccess() {
            Comment comment = new Comment();
            comment.setId(commentId);

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
            when(commentRepository.save(any(Comment.class))).thenAnswer(ans -> ans.getArguments()[0]);

            Comment createdComment = commentService.updateCommentById(commentId, commentDto);

            assertEquals(commentContent, createdComment.getContent());
            assertEquals(LocalDate.now(), createdComment.getUpdatedAt());
        }

        @Test
        void testCommentEntityNotFoundException() {
            when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> commentService.updateCommentById(commentId, commentDto));
        }
    }
}