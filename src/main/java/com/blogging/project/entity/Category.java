package com.blogging.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    UUID id;

    @Column(name="name")
    String name;

    @Column(name="description")
    String description;

    @Column(name="created_at")
    LocalDate createdAt;

    @OneToMany(mappedBy = "category")
    List<Article> articles;
}
