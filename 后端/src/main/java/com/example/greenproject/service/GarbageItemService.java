package com.example.greenproject.service;

import com.example.greenproject.entity.GarbageItem;
import com.example.greenproject.repository.GarbageItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.http.ResponseEntity;

@Service
public class GarbageItemService {
    @Autowired
    private GarbageItemRepository garbageItemRepository;

    public List<GarbageItem> getAllGarbageItems() {
        return garbageItemRepository.findAll();
    }

    public List<GarbageItem> searchGarbageItems(String search) {
        return garbageItemRepository.findByNameContaining(search);
    }

    public GarbageItem addGarbageItem(GarbageItem garbageItem) {
        return garbageItemRepository.save(garbageItem);
    }

    public ResponseEntity<GarbageItem> updateGarbageItem(Long id, GarbageItem garbageItem) {
        return garbageItemRepository.findById(id)
            .map(existingItem -> {
                existingItem.setName(garbageItem.getName());
                existingItem.setCategory(garbageItem.getCategory());
                existingItem.setDescription(garbageItem.getDescription());
                // 新增详细描述和图片处理
                existingItem.setDetailedDescription(garbageItem.getDetailedDescription());
                if (garbageItem.getImage() != null) {
                    existingItem.setImage(garbageItem.getImage());
                }
                GarbageItem updatedItem = garbageItemRepository.save(existingItem);
                return ResponseEntity.ok(updatedItem);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 新增获取单个垃圾项详情方法
    public GarbageItem getGarbageItemById(Long id) {
        return garbageItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garbage item not found"));
    }

    public ResponseEntity<?> deleteGarbageItem(Long id) {
        return garbageItemRepository.findById(id)
            .map(item -> {
                garbageItemRepository.delete(item);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}