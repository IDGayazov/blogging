package com.blogging.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
