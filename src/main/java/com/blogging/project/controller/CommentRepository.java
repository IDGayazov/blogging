package com.blogging.project.controller;

import com.blogging.project.dto.CreateCommentDto;
import com.blogging.project.dto.UpdateCommentDto;
import com.blogging.project.entity.Comment;
import com.blogging.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments(){
        log.info("Request for fetching all comments");
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable("id") UUID commentId){
        log.info("Request for fetching comment by Id: {}", commentId);
        return commentService.getCommentById(commentId);
    }

    @PostMapping
    public void createComment(@RequestBody CreateCommentDto commentDto){
        log.info("Request for saving comment");
        commentService.saveComment(commentDto);
    }

    @PutMapping("/{id}")
    public void updateCommentById(@PathVariable("id") UUID commentId, UpdateCommentDto commentDto){
        log.info("Request for updating comment with Id: {}", commentId);
        commentService.updateCommentById(commentId, commentDto);
    }

    @DeleteMapping
    public void deleteCommentById(@PathVariable("id") UUID commentId){
        log.info("Request for delete comment with Id: {}", commentId);
        commentService.deleteCommentById(commentId);
    }

}
