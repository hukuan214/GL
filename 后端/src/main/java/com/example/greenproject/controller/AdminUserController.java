package com.example.greenproject.controller;

import com.example.greenproject.entity.User;
import com.example.greenproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    public List<User> getAllUsers(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return userRepository.findByUsernameContaining(search);
        }
        return userRepository.findAll();
    }
    
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userData) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUsername(userData.getUsername());
                user.setPassword(userData.getPassword());
                user.setIsAdmin(userData.getIsAdmin()); // 使用整型值
                User updatedUser = userRepository.save(user);
                return ResponseEntity.ok(updatedUser);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}