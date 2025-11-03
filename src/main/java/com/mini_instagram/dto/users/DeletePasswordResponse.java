package com.mini_instagram.dto.users;

public class DeletePasswordResponse {
    private String message;
    private int status;

    public DeletePasswordResponse(String message, int status) {
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
