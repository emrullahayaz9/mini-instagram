package com.mini_instagram.rest;

import com.mini_instagram.Service.AuthService;
import com.mini_instagram.Service.CommentService;
import com.mini_instagram.dto.comment.CommentResponse;
import com.mini_instagram.dto.comment.CreateCommentRequest;
import com.mini_instagram.dto.comment.DeleteCommentResponse;
import com.mini_instagram.entity.User;
import com.mini_instagram.helper.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;
    private final AuthHelper authHelper;

    @Autowired
    public CommentController(CommentService commentService,  AuthService authService, AuthHelper authHelper) {
        this.commentService = commentService;
        this.authService = authService;
        this.authHelper = authHelper;
    }
    @PostMapping("/api/posts/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCommentRequest request
    ) {


        String token = authHelper.getTokenFromHeader(authHeader);
        CommentResponse response = commentService.addComment(id, token, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/api/posts/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable UUID id) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHelper.getTokenFromHeader(authHeader);
        User currentUser = authService.getUserByToken(token);

        commentService.deleteComment(id, currentUser);

        DeleteCommentResponse response = new DeleteCommentResponse(
                "Comment deleted successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
