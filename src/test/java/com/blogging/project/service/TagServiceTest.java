package com.blogging.project.service;

import com.blogging.project.dto.other.CreateTagDto;
import com.blogging.project.entity.Tag;
import com.blogging.project.exceptions.EntityNotFoundException;
import com.blogging.project.repository.TagRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Nested
    class GetTagByIdTest{

        final UUID tagId = UUID.randomUUID();
        final Tag tag = new Tag();
        final String tagName = "Tag name";

        @BeforeEach
        public void setUp(){
            tag.setId(tagId);
            tag.setName(tagName);
        }

        @Test
        void testSuccess() {
            when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
            Tag fetchedTag = tagService.getTagById(tagId);
            assertEquals(tagName, fetchedTag.getName());
            assertEquals(tagId, fetchedTag.getId());
        }

        @Test
        void testThrowTagEntityNotFoundException() {
            when(tagRepository.findById(tagId)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> tagService.getTagById(tagId));
        }
    }

    @Test
    void getAllTags() {
        tagService.getAllTags();
        verify(tagRepository).findAll();
    }

    @Test
    void createTag() {
        final String tagName = "tag name";
        CreateTagDto tagDto = new CreateTagDto(tagName);

        when(tagRepository.save(any(Tag.class))).thenAnswer(ans -> ans.getArguments()[0]);

        Tag createdTag = tagService.createTag(tagDto);

        assertEquals(tagName, createdTag.getName());
        assertEquals(LocalDate.now(), createdTag.getCreatedAt());
    }

    @Test
    void deleteTagById() {
        UUID tagId = UUID.randomUUID();
        tagService.deleteTagById(tagId);
        verify(tagRepository).deleteById(tagId);
    }
}