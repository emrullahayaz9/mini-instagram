package com.mini_instagram.dto.post;

public class DeletePostResponse {
    private String message;
    private int status;

    public DeletePostResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
