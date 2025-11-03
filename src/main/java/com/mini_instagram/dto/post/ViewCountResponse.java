package com.mini_instagram.dto.post;

public class ViewCountResponse {
    private String message;
    private int viewCount;

    public ViewCountResponse(String message, int viewCount) {
        this.message = message;
        this.viewCount = viewCount;
    }

    public String getMessage() {
        return message;
    }

    public int getViewCount() {
        return viewCount;
    }
}
