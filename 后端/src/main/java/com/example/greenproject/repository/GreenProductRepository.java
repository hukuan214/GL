package com.example.greenproject.repository;

import com.example.greenproject.entity.GreenProduct;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GreenProductRepository extends JpaRepository<GreenProduct, Long> {
    List<GreenProduct> findByNameContaining(String name);
}