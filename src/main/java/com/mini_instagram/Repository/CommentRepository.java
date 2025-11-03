package com.mini_instagram.Repository;

import com.mini_instagram.entity.Comment;
import com.mini_instagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPost(Post post);
}
