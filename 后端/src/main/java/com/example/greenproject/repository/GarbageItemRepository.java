package com.example.greenproject.repository;

import com.example.greenproject.entity.GarbageItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GarbageItemRepository extends JpaRepository<GarbageItem, Long> {
    List<GarbageItem> findByNameContaining(String name);
}