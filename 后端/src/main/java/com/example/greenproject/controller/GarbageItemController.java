package com.example.greenproject.controller;

import com.example.greenproject.entity.GarbageItem;
import com.example.greenproject.service.GarbageItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
// 为了解决 CrossOrigin 无法解析的问题，需要导入 CrossOrigin 注解。
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
@CrossOrigin
@RestController
@RequestMapping("/api/garbage-items")
public class GarbageItemController {
    @Autowired
    private GarbageItemService garbageItemService;

    @GetMapping
    public List<GarbageItem> getAllGarbageItems() {
        return garbageItemService.getAllGarbageItems();
    }

    @GetMapping("/search")
    public List<GarbageItem> searchGarbageItems(@RequestParam String search) {
        return garbageItemService.searchGarbageItems(search);
    }

    @GetMapping("/{id}")
    public GarbageItem getGarbageItemById(@PathVariable Long id) {
        return garbageItemService.getGarbageItemById(id);
    }

    @PostMapping
    public GarbageItem addGarbageItem(@RequestBody GarbageItem garbageItem) {
        return garbageItemService.addGarbageItem(garbageItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarbageItem> updateGarbageItem(
            @PathVariable Long id, 
            @RequestBody GarbageItem garbageItem) {
        return garbageItemService.updateGarbageItem(id, garbageItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGarbageItem(@PathVariable Long id) {
        return garbageItemService.deleteGarbageItem(id);
    }
}