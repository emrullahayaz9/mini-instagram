package com.mini_instagram.rest;

import com.mini_instagram.Service.AuthService;
import com.mini_instagram.dto.auth.*;
import com.mini_instagram.entity.AuthToken;
import com.mini_instagram.entity.User;
import com.mini_instagram.helper.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthHelper authHelper;
    @Autowired
    public AuthController(AuthService authService,  AuthHelper authHelper) {
        this.authService = authService;
        this.authHelper = authHelper;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        User newUser = authService.signup(request.getUsername(), request.getPassword(), request.getAuthKey());
        SignupResponse response = new SignupResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getRole().toString(),
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthToken token = authService.login(request.getUsername(), request.getPassword());

        LoginResponse response = new LoginResponse(
                token.getToken(),
                token.getExpiryDate(),
                token.getUser().getUsername(),
                token.getUser().getRole().toString()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.getToken());
        LogoutResponse response = new LogoutResponse(
                "Logout successful",
                HttpStatus.ACCEPTED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHelper.getTokenFromHeader(authHeader);
        User user = authService.getUserByToken(token);
        UserMeResponse response = new UserMeResponse(
                user.getUsername(),
                user.getRole().toString()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
