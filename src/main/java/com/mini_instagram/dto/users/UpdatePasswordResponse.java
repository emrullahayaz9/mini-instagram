package com.mini_instagram.dto.users;

public class UpdatePasswordResponse {
    private String message;
    private int status;

    public UpdatePasswordResponse(String message, int status) {
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
