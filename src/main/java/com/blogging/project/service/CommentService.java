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
    private static final String USER_NOT_FOUND_MESSAGE = "User with Id: %s not found";
    private static final String ARTICLE_NOT_FOUND_MESSAGE = "Article with Id: %s not found";

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public Comment getCommentById(UUID commentId){
        log.info("Fetching comment by Id: {}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(COMMENT_NOT_FOUND_MESSAGE, commentId)));
    }

    public List<Comment> getAllComments(){
        log.info("Fetching all comments");
        return commentRepository.findAll();
    }

    public void deleteCommentById(UUID commentId){
        commentRepository.deleteById(commentId);
        log.info("Deleting comment by Id: {}", commentId);
    }

    public void saveComment(CreateCommentDto commentDto){
        Comment comment = commentMapper.toComment(commentDto);

        Optional<User> optionalUser = userRepository.findById(commentDto.userId());
        optionalUser.ifPresentOrElse(comment::setUser, () -> {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, commentDto.userId()));
        });

        Optional<Article> optionalArticle = articleRepository.findById(commentDto.articleId());
        optionalArticle.ifPresentOrElse(comment::setArticle, () -> {
            throw new EntityNotFoundException(String.format(ARTICLE_NOT_FOUND_MESSAGE, commentDto.articleId()));
        });

        comment.setCreatedAt(LocalDate.now());
        comment.setUpdatedAt(LocalDate.now());

        commentRepository.save(comment);
        log.info("Save comment with Id: {}", comment.getId());
    }

    public void updateCommentById(UUID commentId, UpdateCommentDto updateCommentDto){
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        Comment comment = optionalComment.orElseThrow(
                () -> new EntityNotFoundException(String.format(COMMENT_NOT_FOUND_MESSAGE, commentId))
        );

        comment.setUpdatedAt(updateCommentDto.updatedAt());
        comment.setContent(updateCommentDto.content());

        commentRepository.save(comment);
        log.info("Update comment with Id: {}", commentId);
    }

}
