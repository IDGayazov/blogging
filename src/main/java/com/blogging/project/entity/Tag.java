package com.blogging.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="Tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    UUID id;

    @Column(name="name")
    String name;

    @Column(name="created_at")
    LocalDate createdAt;

    @ManyToMany
    @JoinTable(
        name = "Article_Tags",
        joinColumns = @JoinColumn(name="tag_id"),
        inverseJoinColumns = @JoinColumn(name="article_id")
    )
    List<Article> articles;

}
