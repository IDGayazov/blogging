package com.blogging.project.service;

import com.blogging.project.dto.CreateTagDto;
import com.blogging.project.entity.Tag;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.repository.TagRepository;
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
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTagById(UUID tagId){
        log.info("fetch tag by Id: {}", tagId);
        return tagRepository.findById(tagId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Tag with Id: %s not found", tagId))
        );
    }

    public List<Tag> getAllTags(){
        log.info("fetch all tags");
        return tagRepository.findAll();
    }

    public void createTag(CreateTagDto tagDto){
        Tag tag = new Tag();
        tag.setName(tagDto.name());
        tag.setCreatedAt(LocalDate.now());
        tagRepository.save(tag);
        log.info("Tag with Id: {} was saved", tag.getId());
    }

    public void deleteTagById(UUID tagId){
        tagRepository.deleteById(tagId);
        log.info("Tag with Id: {} was deleted", tagId);
    }
}
