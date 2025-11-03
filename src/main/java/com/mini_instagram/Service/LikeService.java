package com.mini_instagram.Service;

import com.mini_instagram.Repository.LikeRepository;
import com.mini_instagram.Repository.PostRepository;
import com.mini_instagram.entity.Like;
import com.mini_instagram.entity.Post;
import com.mini_instagram.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service

public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Autowired
    public LikeService(LikeRepository likeRepository, PostRepository postRepository, AuthService authService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.authService = authService;
    }

    public void likePost(UUID postId, String token) {
        User user = authService.getUserByToken(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (likeRepository.findByPostAndUser(post, user).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already liked this post");
        }

        Like like = new Like(post, user);
        likeRepository.save(like);

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    public void unlikePost(UUID postId, String token) {
        User user = authService.getUserByToken(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Like like = likeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found"));

        likeRepository.delete(like);

        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
    }
}
