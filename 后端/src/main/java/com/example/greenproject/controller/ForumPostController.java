package com.example.greenproject.controller;

import com.example.greenproject.entity.ForumPost;
import com.example.greenproject.repository.ForumPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// 为了解决 CrossOrigin 无法解析的问题，需要导入 CrossOrigin 注解。
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Date;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ForumPostController {

    @Autowired
    private ForumPostRepository forumPostRepository;

    // 普通用户发帖接口
    @PostMapping("/forum-postsqd")
    public ForumPost createPost(@RequestBody ForumPost post) {
        post.setPost_date(new Date());
        return forumPostRepository.save(post);
    }

    // 管理员发帖接口
    @PostMapping("/admin/forum-posts")
    public ForumPost createPostByAdmin(@RequestBody ForumPost post) {
        post.setPost_date(new Date());
        return forumPostRepository.save(post);
    }

    // 获取所有帖子(普通用户)
    @GetMapping("/forum-posts")
    public List<ForumPost> getPublicForumPosts() {
        return forumPostRepository.findAll();
    }

    // 管理员获取所有帖子
    @GetMapping("/admin/forum-posts")
    public List<ForumPost> getAllForumPosts() {
        return forumPostRepository.findAll();
    }

    // 更新帖子
    @PutMapping("/admin/forum-posts/{id}")
    public ForumPost updateForumPost(@PathVariable Integer id, @RequestBody ForumPost postDetails) {
        ForumPost post = forumPostRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        // 新增发布时间修改
        if(postDetails.getPost_date() != null) {
            post.setPost_date(postDetails.getPost_date());
        }
        return forumPostRepository.save(post);
    }

    // 删除帖子
    @DeleteMapping("/admin/forum-posts/{id}")
    public ResponseEntity<?> deleteForumPost(@PathVariable Integer id) {
        ForumPost post = forumPostRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        forumPostRepository.delete(post);
        return ResponseEntity.ok().build();
    }
}