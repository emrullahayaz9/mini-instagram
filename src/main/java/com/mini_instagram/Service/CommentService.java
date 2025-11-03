package com.mini_instagram.Service;

import com.mini_instagram.Repository.AuthTokenRepository;
import com.mini_instagram.Repository.CommentRepository;
import com.mini_instagram.Repository.PostRepository;
import com.mini_instagram.dto.comment.CommentResponse;
import com.mini_instagram.dto.comment.CreateCommentRequest;
import com.mini_instagram.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthTokenRepository authTokenRepository;

    @Autowired // no needed explicitly because there is only 1 constructor
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, AuthTokenRepository authTokenRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authTokenRepository = authTokenRepository;
    }
    @Transactional
    public CommentResponse addComment(UUID postId, String tokenValue, CreateCommentRequest request) {
        AuthToken token = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(tokenValue, true, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token."));

        User user = token.getUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setText(request.getContent());
        commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return new CommentResponse(comment.getId(), comment.getText(), user.getUsername());
    }
    public List<CommentResponse> getCommentsByPostId(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));

        List<Comment> comments = commentRepository.findByPost(post);

        return comments.stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getUsername()
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteComment(UUID commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found."));

        Post post = comment.getPost();

        boolean isCommentAuthor = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isPostAuthor = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!(isCommentAuthor || isPostAuthor || isAdmin)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this comment.");
        }

        commentRepository.delete(comment);
    }
}
