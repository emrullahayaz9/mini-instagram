package com.mini_instagram.rest;

import com.mini_instagram.Repository.AuthTokenRepository;
import com.mini_instagram.Service.UsersService;
import com.mini_instagram.dto.users.DeletePasswordResponse;
import com.mini_instagram.dto.users.UpdatePasswordRequest;
import com.mini_instagram.dto.users.UpdatePasswordResponse;
import com.mini_instagram.entity.AuthToken;
import com.mini_instagram.entity.User;
import com.mini_instagram.helper.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;
    private final AuthTokenRepository authTokenRepository;
    private final AuthHelper authHelper;

    @Autowired
    public UsersController(UsersService usersService,  AuthTokenRepository authTokenRepository,  AuthHelper authHelper) {
        this.usersService = usersService;
        this.authTokenRepository = authTokenRepository;
        this.authHelper = authHelper;

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = usersService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/me/password")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdatePasswordRequest request) {
        String token = authHelper.getTokenFromHeader(authHeader);
        AuthToken authToken = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(token, true, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token"));

        User user = authToken.getUser();
        usersService.updatePassword(user, request.getOldPassword(), request.getNewPassword());

        UpdatePasswordResponse response = new UpdatePasswordResponse(
                "Password updated successfully",
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<DeletePasswordResponse> deleteMyAccount(@RequestHeader("Authorization") String authHeader) {
        String token = authHelper.getTokenFromHeader(authHeader);
        AuthToken authToken = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(token, true, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token"));

        User user = authToken.getUser();
        usersService.deleteMyAccount(user);
        DeletePasswordResponse response = new DeletePasswordResponse(
                "User deleted successfully.",
                HttpStatus.OK.value()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<String> deleteUserByAdmin(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String userId) {
        String token = authHelper.getTokenFromHeader(authHeader);
        AuthToken authToken = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(token, true, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token"));
        User user = authToken.getUser();

        usersService.deleteUserByAdmin(user, userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}