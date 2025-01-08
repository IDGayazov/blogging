package com.blogging.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="Article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    UUID id;

    @Column(name="title")
    String title;

    @Column(name="content")
    String content;

    @Column(name="created_at")
    LocalDate createdAt;

    @Column(name="updated_at")
    LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    Category category;

    @ManyToMany(mappedBy = "articles")
    List<Tag> tags;
}
