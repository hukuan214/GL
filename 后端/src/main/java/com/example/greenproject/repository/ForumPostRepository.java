package com.example.greenproject.repository;

import com.example.greenproject.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository<ForumPost, Integer> {
}