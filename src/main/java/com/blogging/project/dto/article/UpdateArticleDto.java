package com.blogging.project.dto.article;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile; 

public record UpdateArticleDto(
        String title,
        String content,
        MultipartFile image,
        UUID categoryId
) {}
