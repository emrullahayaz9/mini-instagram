package com.mini_instagram.dto.post;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreatePostResponse {
    private UUID id;
    private String author;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    private int status;

    public CreatePostResponse(UUID id, String author, String imageUrl, String description, LocalDateTime createdAt, int status) {
        this.id = id;
        this.author = author;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getStatus() {
        return status;
    }
}
