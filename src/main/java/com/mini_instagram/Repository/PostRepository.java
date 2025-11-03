package com.mini_instagram.Repository;

import com.mini_instagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findById(UUID id);
    List<Post> findByActiveTrue();
}
