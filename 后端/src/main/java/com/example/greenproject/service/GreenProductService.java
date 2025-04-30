package com.example.greenproject.service;

import com.example.greenproject.entity.GreenProduct;
import com.example.greenproject.repository.GreenProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GreenProductService {
    @Autowired
    private GreenProductRepository greenProductRepository;

    public List<GreenProduct> getAllGreenProducts() {
        return greenProductRepository.findAll();
    }

    public GreenProduct getGreenProductById(Long id) {
        return greenProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<GreenProduct> searchGreenProducts(String search) {
        return greenProductRepository.findByNameContaining(search);
    }
    
    public GreenProduct saveProduct(GreenProduct product) {
        return greenProductRepository.save(product);
    }
}