package com.mini_instagram.dto.post;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetPostResponse {
    private UUID id;
    private String description;
    private String imageUrl;
    private String authorUsername;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private List<CommentResponse> comments = new ArrayList<>();

    public GetPostResponse(UUID id, String description, String imageUrl, String authorUsername, int viewCount, int likeCount, int commentCount) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.authorUsername = authorUsername;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.comments = comments;
    }
    public GetPostResponse(){}
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
