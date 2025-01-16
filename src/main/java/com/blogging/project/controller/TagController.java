package com.blogging.project.controller;

import com.blogging.project.dto.CreateTagDto;
import com.blogging.project.entity.Tag;
import com.blogging.project.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable("id") UUID tagId){
        log.info("Receive request for fetch tag with Id: {}", tagId);
        return tagService.getTagById(tagId);
    }

    @GetMapping
    public List<Tag> getAllTags(){
        log.info("Receive request for fetching all tags");
        return tagService.getAllTags();
    }

    @PostMapping
    public void createTag(@RequestBody CreateTagDto tagDto){
        log.info("Receive request for creating new tag with name: {}", tagDto.name());
        tagService.createTag(tagDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTagByID(@PathVariable("id") UUID tagId){
        log.info("Receive request for delete tag with Id: {}", tagId);
        tagService.deleteTagById(tagId);
    }
}
