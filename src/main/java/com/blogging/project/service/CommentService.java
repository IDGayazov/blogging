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
public class CommentService {

    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment with Id: %s not found";

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final UserService userService;
    private final ArticleService articleService;

    public Comment getCommentById(UUID commentId){
        log.info("Fetching comment by Id: {}", commentId);
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(String.format(COMMENT_NOT_FOUND_MESSAGE, commentId))
        );
    }

    public List<Comment> getAllComments(){
        log.info("Fetching all comments");
        return commentRepository.findAll();
    }

    public void deleteCommentById(UUID commentId){
        commentRepository.deleteById(commentId);
        log.info("Deleting comment by Id: {}", commentId);
    }

    public Comment saveComment(CreateCommentDto commentDto){
        Comment comment = commentMapper.toComment(commentDto);

        User user = userService.getUserById(commentDto.userId());
        Article article = articleService.getArticleById(commentDto.articleId());

        comment.setUser(user);
        comment.setArticle(article);
        comment.setCreatedAt(LocalDate.now());
        comment.setUpdatedAt(LocalDate.now());

        Comment createdComment = commentRepository.save(comment);
        log.info("Save comment with Id: {}", comment.getId());

        return createdComment;
    }

    public Comment updateCommentById(UUID commentId, UpdateCommentDto updateCommentDto){
        Comment comment = getCommentById(commentId);

        comment.setUpdatedAt(LocalDate.now());
        comment.setContent(updateCommentDto.content());

        Comment updatedComment = commentRepository.save(comment);
        log.info("Update comment with Id: {}", commentId);

        return updatedComment;
    }
}
