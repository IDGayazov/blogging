package com.blogging.project.controller;

import com.blogging.project.dto.CreateCommentDto;
import com.blogging.project.dto.UpdateCommentDto;
import com.blogging.project.entity.Comment;
import com.blogging.project.service.CommentService;
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
@Slf4j
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

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

    @DeleteMapping("/id")
    public void deleteCommentById(@PathVariable("id") UUID commentId){
        log.info("Request for delete comment with Id: {}", commentId);
        commentService.deleteCommentById(commentId);
    }

}
