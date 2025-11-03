package com.mini_instagram.dto.auth;

import java.util.UUID;

public class SignupResponse {
    private UUID id;
    private String username;
    private String role;
    private int status;

    public SignupResponse(UUID id, String username, String role, int status) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
