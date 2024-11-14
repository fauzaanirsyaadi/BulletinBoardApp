package com.example.BulletinApp.controller;

import com.example.BulletinApp.model.Post;
import com.example.BulletinApp.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Tag(name = "Post", description = "Post management APIs")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(summary = "List all posts", description = "Get a list of all posts")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "text/html",
                    schema = @Schema(implementation = String.class)))
    @GetMapping("/")
    public String listPosts(Model model,
                            @RequestParam(defaultValue = "createdAt") String sort,
                            @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortObj = Sort.by(sortDirection, sort);
        List<Post> posts = postService.getAllPosts(sortObj);
        model.addAttribute("posts", posts);
        return "postList";

    }

    @Operation(summary = "View a post", description = "Get details of a specific post by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "text/html",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "302", description = "Post not found, redirect to home page")
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        if (post != null) {
            post = postService.incrementViews(post);
            model.addAttribute("post", post);
            return "postDetails";
        }
        return "redirect:/";
    }

    @Operation(summary = "Show create post form", description = "Displays the form for creating a new post")
    @ApiResponse(responseCode = "200", description = "Form displayed successfully",
            content = @Content(mediaType = "text/html",
                    schema = @Schema(implementation = String.class)))
    @GetMapping("/post/new")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        return "createPost";
    }

    @Operation(summary = "Create a new post", description = "Creates a new post with the provided details")
    @ApiResponse(responseCode = "302", description = "Post created successfully, redirect to home page")
    @ApiResponse(responseCode = "200", description = "Error in post creation, form redisplayed",
            content = @Content(mediaType = "text/html",
                    schema = @Schema(implementation = String.class)))
    @PostMapping("/post/new")
    public String createPost(@ModelAttribute Post post) {
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        post.setViews(0);

        // Ensure content is not null
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            // Handle the error, perhaps by adding a model attribute and returning to the form
            return "createPost";
        }

        postService.getAllPosts(Sort.by(Sort.Direction.DESC, "id")).stream().findFirst().ifPresent(lastPost -> post.setId(lastPost.getId() + 1));

        // Save the post
        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/post/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/";
        }
        model.addAttribute("post", post);
        return "editPost";
    }

    @Operation(summary = "Update a post", description = "Updates an existing post with the given ID")
    @ApiResponse(responseCode = "200", description = "Post updated successfully",
            content = @Content(mediaType = "text/html"))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @PostMapping("/post/{id}/edit")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, @RequestParam String password, Model model) {
        boolean updated = postService.updatePost(id, post, password);
        if (updated) {
            return "redirect:/post/" + id;
        } else {
            model.addAttribute("error", "Incorrect password or post not found. Please try again.");
            model.addAttribute("post", postService.getPostById(id));
            return "editPost";
        }
    }

    @Operation(summary = "Delete a post", description = "Deletes an existing post with the given ID")
    @ApiResponse(responseCode = "200", description = "Post deleted successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @PostMapping("/post/{id}/delete")
    @ResponseBody
    public Map<String, Boolean> deletePost(@PathVariable Long id, @RequestParam String password) {
        boolean deleted = postService.deletePost(id, password);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", deleted);
        return response;
    }
}