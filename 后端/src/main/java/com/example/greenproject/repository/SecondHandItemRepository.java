package com.example.greenproject.repository;

import com.example.greenproject.entity.SecondHandItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondHandItemRepository extends JpaRepository<SecondHandItem, Integer> {
}