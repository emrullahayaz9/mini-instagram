package com.mini_instagram.dto.auth;

import java.time.LocalDateTime;

public class LoginResponse {
    private String token;
    private LocalDateTime expiryDate;
    private String username;
    private String role;

    public LoginResponse(String token, LocalDateTime expiryDate, String username, String role) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }
}
