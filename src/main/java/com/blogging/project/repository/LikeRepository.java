package com.blogging.project.repository;

import com.blogging.project.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    void deleteByUser_IdAndArticle_Id(UUID userId, UUID articleId);
}
