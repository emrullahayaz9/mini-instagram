package com.mini_instagram.Repository;

import com.mini_instagram.entity.Like;
import com.mini_instagram.entity.Post;
import com.mini_instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByPostAndUser(Post post, User user);

}
