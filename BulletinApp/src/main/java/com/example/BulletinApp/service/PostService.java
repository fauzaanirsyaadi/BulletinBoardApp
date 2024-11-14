package com.example.BulletinApp.service;

import com.example.BulletinApp.model.Post;
import com.example.BulletinApp.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts(Sort sort) {
        return postRepository.findByDeletedFalse(sort);
    }

    public Post getPostById(Long id) {
        return (Post) postRepository.findByIdAndDeletedFalse(id).orElse(null);
    }

    @Transactional
    public Post incrementViews(Post postId) {
        Post post = postRepository.findById(postId.getId()).orElse(null);
        if (post != null) {
            post.setViews(post.getViews() + 1);
            return postRepository.save(post);
        }
        return null;
    }

    public void savePost(Post post) {
        if (post.getCreatedAt() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        postRepository.save(post);
    }

    public boolean deletePost(Long id, String password) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (password.equals(existingPost.getPassword())) {
                existingPost.setDeleted(true);
                postRepository.save(existingPost);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean updatePost(Long id, Post updatedPost, String password) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (password.equals(existingPost.getPassword())) {
                existingPost.setTitle(updatedPost.getTitle());
                existingPost.setAuthorName(updatedPost.getAuthorName());
                existingPost.setContent(updatedPost.getContent());
                existingPost.setModifiedAt(LocalDateTime.now());
                postRepository.save(existingPost);
                return true;
            }
        }
        return false;
    }
}