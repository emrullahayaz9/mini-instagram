package com.mini_instagram.Service;

import com.mini_instagram.Repository.AuthTokenRepository;
import com.mini_instagram.Repository.UserRepository;
import com.mini_instagram.entity.AuthToken;
import com.mini_instagram.entity.Role;
import com.mini_instagram.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;

    @Value("${app_user.admin.key}")
    private String systemAdminKey;
    @Autowired
    public AuthService(UserRepository userRepository, AuthTokenRepository authTokenRepository) {
        this.userRepository = userRepository;
        this.authTokenRepository = authTokenRepository;
    }

    private static final long TOKEN_VALIDITY_HOURS = 1;

    @Transactional
    public User signup(String username, String rawPassword, String admin_key) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exists.");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(rawPassword);
        if (admin_key != null && admin_key.equals(systemAdminKey)) {
            newUser.setRole(Role.ADMIN);
        } else {
            newUser.setRole(Role.USER);
        }
        return userRepository.save(newUser);
    }

    @Transactional
    public AuthToken login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "username or password is wrong."));

        if (!rawPassword.equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "username or password is wrong.");
        }

        Optional<AuthToken> existingTokenOpt = authTokenRepository.findByUserAndIsActive(user, true);

        AuthToken token;
        if (existingTokenOpt.isPresent()) {
            token = existingTokenOpt.get();
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(LocalDateTime.now().plusHours(TOKEN_VALIDITY_HOURS));
            token.setActive(true);
        } else {
            token = new AuthToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(LocalDateTime.now().plusHours(TOKEN_VALIDITY_HOURS));
            token.setActive(true);
        }

        return authTokenRepository.save(token);
    }


    @Transactional
    public void logout(String tokenValue) {
        AuthToken token = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(tokenValue, true, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid token or token is expired."));

        token.setActive(false);
        authTokenRepository.save(token);
    }

    public User getUserByToken(String token) {
        AuthToken authToken = authTokenRepository.findByTokenAndIsActiveAndExpiryDateAfter(token, true, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token"));
        return authToken.getUser();
    }

}
