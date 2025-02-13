package com.blogging.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name="avatar_url")
    String avatarUrl;

    @Column(name="created_at")
    LocalDate createdAt;

    @Column(name="updated_at")
    LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    Category category;

    @ManyToMany(mappedBy = "articles")
    List<Tag> tags;
}
