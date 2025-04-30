package com.example.greenproject.controller;

import com.example.greenproject.entity.User;
import com.example.greenproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            response.put("error", "Username already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        response.put("message", "Registration successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/login")
public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
    Map<String, String> response = new HashMap<>();
    Optional<User> foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    if (foundUser.isPresent()) {
        response.put("role", foundUser.get().getIsAdmin() == 1 ? "admin" : "user");
        return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
        response.put("error", "Invalid username or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
}