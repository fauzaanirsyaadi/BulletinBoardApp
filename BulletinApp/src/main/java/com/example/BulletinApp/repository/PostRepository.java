package com.example.BulletinApp.repository;

import com.example.BulletinApp.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Object> findByIdAndDeletedFalse(Long id);

    List<Post> findByDeletedFalse(Sort sort);
}