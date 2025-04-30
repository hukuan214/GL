package com.example.greenproject.controller;

import com.example.greenproject.entity.GreenProduct;
import com.example.greenproject.repository.GreenProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    
    @Autowired
    private GreenProductRepository productRepository;
    
    @GetMapping
    public List<GreenProduct> getAllProducts() {
        return productRepository.findAll();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GreenProduct> updateProduct(@PathVariable Long id, @RequestBody GreenProduct productData) {
        return productRepository.findById(id)
            .map(product -> {
                product.setName(productData.getName());
                product.setDescription(productData.getDescription());
                product.setDetailedDescription(productData.getDetailedDescription());
                if (productData.getImage() != null) {
                    product.setImage(productData.getImage());
                }
                GreenProduct updatedProduct = productRepository.save(product);
                return ResponseEntity.ok(updatedProduct);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public GreenProduct addProduct(@RequestBody GreenProduct product) {
        return productRepository.save(product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(product -> {
                productRepository.delete(product);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}