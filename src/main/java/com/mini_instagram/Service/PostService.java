package com.mini_instagram.Service;

import com.mini_instagram.Repository.AuthTokenRepository;
import com.mini_instagram.Repository.PostRepository;
import com.mini_instagram.dto.post.CommentResponse;
import com.mini_instagram.dto.post.GetPostResponse;
import com.mini_instagram.entity.AuthToken;
import com.mini_instagram.entity.Post;
import com.mini_instagram.entity.Role;
import com.mini_instagram.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final AuthTokenRepository authTokenRepository;
    private final PostRepository postRepository;
    private final String UPLOAD_DIR = "uploads/";

    @Autowired
    public PostService(AuthTokenRepository authTokenRepository,  PostRepository postRepository) {
        this.authTokenRepository = authTokenRepository;
        this.postRepository = postRepository;
    }
    public GetPostResponse getPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post not found with id: " + postId
                ));
        GetPostResponse dto = new GetPostResponse();
        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setImageUrl(post.getImageUrl());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setViewCount(post.getViewCount());
        dto.setLikeCount(post.getLikes().size());
        dto.setCommentCount(post.getComments().size());
        List<CommentResponse> commentResponses = post.getComments().stream()
                .map(c -> new CommentResponse(c.getId(), c.getText(), c.getAuthor().getUsername()))
                .toList();
        dto.setComments(commentResponses);
        return dto;
    }
    @Transactional
    public Post createPost(String requestToken, String description, MultipartFile imageFile) {

        AuthToken token = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(requestToken, true,  LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or inactive token."));

        User user = token.getUser();

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload directory cannot be created");
        }
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        try {
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
        }
        Post post = new Post();
        post.setAuthor(user);
        post.setImageUrl("/uploads/"+fileName);
        post.setDescription(description);
        post.setActive(true);
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(UUID postId, String description, MultipartFile imageFile, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));

        if (!post.getAuthor().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this post.");
        }

        if (!Objects.equals(description,"")) {
            post.setDescription(description);
        }
        if (!Objects.equals(imageFile.getOriginalFilename(), "")) {
            try {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                post.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
            }
        }

        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(UUID postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));

        if (!post.getAuthor().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this post.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public int incrementViewCount(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found."));

        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return post.getViewCount();
    }
    public List<GetPostResponse> getAllActivePosts() {
        List<Post> posts = postRepository.findByActiveTrue();

        return posts.stream().map(post -> {
            GetPostResponse response = new GetPostResponse();
            response.setId(post.getId());
            response.setDescription(post.getDescription());
            response.setImageUrl(post.getImageUrl());
            response.setAuthorUsername(post.getAuthor().getUsername());
            response.setViewCount(post.getViewCount());
            response.setLikeCount(post.getLikeCount());
            response.setCommentCount(post.getCommentCount());

            return response;
        }).collect(Collectors.toList());
    }
}
