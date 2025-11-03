package com.mini_instagram.dto.post;

public class UpdatePostRequest {
    private String description;
    private String imageUrl;

    public UpdatePostRequest() {}

    public UpdatePostRequest(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
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
}

