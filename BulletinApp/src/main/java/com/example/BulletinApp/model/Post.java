package com.example.BulletinApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String password;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    public void setDeleted(boolean b) {
        this.deleted = b;
        modifiedAt = LocalDateTime.now();
        if (b) {
            createdAt = modifiedAt;
        }
        views = 0;
        content = "This post has been deleted.";
        title = "Deleted Post";
        authorName = "Admin";
        password = "admin";
    }

}