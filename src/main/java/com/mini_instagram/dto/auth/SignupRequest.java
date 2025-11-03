package com.mini_instagram.dto.auth;

public class SignupRequest {
    private String username;
    private String password;
    private String auth_key;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthKey() {
        return auth_key;
    }
    public void setAuthKey(String authKey) {
        this.auth_key = authKey;
    }
}

