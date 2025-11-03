package com.mini_instagram.rest;

import com.mini_instagram.Service.AuthService;
import com.mini_instagram.Service.PostService;
import com.mini_instagram.dto.post.*;
import com.mini_instagram.entity.Post;
import com.mini_instagram.entity.User;
import com.mini_instagram.helper.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final AuthService authService;
    private final AuthHelper authHelper;

    @Autowired
    public PostController(PostService postService,  AuthService authService,  AuthHelper authHelper) {
        this.postService = postService;
        this.authService = authService;
        this.authHelper = authHelper;

    }
    @PostMapping
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String authHeader, @RequestParam("description") String description, @RequestParam("image") MultipartFile imageFile) {
        String token = authHelper.getTokenFromHeader(authHeader);
        Post post = postService.createPost(token, description, imageFile);
        return new ResponseEntity<>("Post created successfully.", HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable UUID id) {
        GetPostResponse post = postService.getPost(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePostResponse> updatePost(
            @PathVariable UUID id,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile,
            @RequestHeader("Authorization") String requestHeader
    ) {
        String token = authHelper.getTokenFromHeader(requestHeader);
        User currentUser = authService.getUserByToken(token);
        Post updatedPost = postService.updatePost(id, description, imageFile, currentUser);
        UpdatePostResponse response = new UpdatePostResponse();
        response.setId(updatedPost.getId());
        response.setDescription(updatedPost.getDescription());
        response.setImageUrl(updatedPost.getImageUrl());
        response.setAuthorUsername(updatedPost.getAuthor().getUsername());
        response.setViewCount(updatedPost.getViewCount());
        response.setLikeCount(updatedPost.getLikeCount());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeletePostResponse> deletePost(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String requestHeader
    ) {
        String token = authHelper.getTokenFromHeader(requestHeader);
        User currentUser = authService.getUserByToken(token);
        postService.deletePost(id, currentUser);

        DeletePostResponse response = new DeletePostResponse(
                "Post deleted successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/{id}/view")
    public ResponseEntity<ViewCountResponse> incrementViewCount(@PathVariable UUID id) {
        int newViewCount = postService.incrementViewCount(id);
        ViewCountResponse response = new ViewCountResponse("View count increased successfully", newViewCount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<GetPostResponse>> getAllActivePosts() {
        List<GetPostResponse> posts = postService.getAllActivePosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
