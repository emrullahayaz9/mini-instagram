package com.mini_instagram.dto.post;

import java.util.UUID;

public class CommentResponse {
    private UUID id;
    private String content;
    private String authorUsername;

    public CommentResponse(UUID id, String content, String authorUsername) {
        this.id = id;
        this.content = content;
        this.authorUsername = authorUsername;
    }

    public CommentResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
}
