package com.example.greenproject.controller;

import com.example.greenproject.entity.SecondHandItem;
import com.example.greenproject.repository.SecondHandItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/second-hand-items")
@CrossOrigin
public class SecondHandItemController {
    @Autowired
    private SecondHandItemRepository repository;

    @GetMapping
    public List<SecondHandItem> getAllItems() {
        return repository.findAll();
    }

    // 修改POST映射路径，去掉重复的/api/second-hand-items
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody SecondHandItem item) {
        try {
            item.setPublish_date(new Date());
            // 保留手机号格式验证但取消加密
            if(item.getPhone_number() == null || !item.getPhone_number().matches("^1[3-9]\\d{9}$")) {
                return ResponseEntity.badRequest().body("手机号格式不正确");
            }
            SecondHandItem savedItem = repository.save(item);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SecondHandItem> updateItem(
            @PathVariable Integer id, @RequestBody SecondHandItem itemData) {
        return repository.findById(id)
            .map(item -> {
                item.setItem_name(itemData.getItem_name());
                item.setItem_description(itemData.getItem_description());
                item.setPhone_number(itemData.getPhone_number()); // 新增手机号更新
                SecondHandItem updatedItem = repository.save(item);
                return ResponseEntity.ok(updatedItem);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        return repository.findById(id)
            .map(item -> {
                repository.delete(item);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{id}/image")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {
        try {
            SecondHandItem item = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            
            item.setImage(file.getBytes());
            repository.save(item);
            
            // 返回包含图片base64编码的响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("image", Base64.getEncoder().encodeToString(file.getBytes()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecondHandItem> getItemById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
