package com.mini_instagram.rest;

import com.mini_instagram.Service.LikeService;
import com.mini_instagram.helper.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class LikeController {
    private final LikeService likeService;
    private final AuthHelper authHelper;

    @Autowired
    public LikeController(LikeService likeService,  AuthHelper authHelper) {
        this.likeService = likeService;
        this.authHelper = authHelper;
    }
    @PostMapping("/api/posts/{id}/likes")
    public ResponseEntity<String> likePost(@PathVariable UUID id,
                                           @RequestHeader("Authorization") String authHeader) {
        String token = authHelper.getTokenFromHeader(authHeader);
        likeService.likePost(id, token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post liked successfully");
    }

    @DeleteMapping("/api/posts/{id}/likes")
    public ResponseEntity<String> unlikePost(@PathVariable UUID id,
                                             @RequestHeader("Authorization") String authHeader) {
        String token = authHelper.getTokenFromHeader(authHeader);
        likeService.unlikePost(id, token);
        return ResponseEntity.ok("Like removed successfully");
    }
}
