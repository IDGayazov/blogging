package com.blogging.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @Column(name="email")
    String email;

    @Column(name="password")
    String password;

    @Column(name="name")
    String name;

    @Column(name="avatar_url")
    String avatarUrl;

    @Column(name="bio")
    String bio;

    @Column(name="created_at")
    LocalDate createdAt;

    @Column(name="updated_at")
    LocalDate updatedAt;

    @OneToMany(mappedBy="user")
    List<Article> articles;

    @OneToMany(mappedBy="user")
    List<Comment> comments;
}
