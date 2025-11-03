package com.mini_instagram.Repository;

import com.mini_instagram.entity.AuthToken;
import com.mini_instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    Optional<AuthToken> findByTokenAndIsActiveAndExpiryDateAfter(String token, boolean isActive, LocalDateTime now);
    Optional<AuthToken> findByUserAndIsActive(User user,boolean isActive);
}
