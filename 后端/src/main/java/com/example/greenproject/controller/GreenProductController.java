package com.example.greenproject.controller;

import com.example.greenproject.entity.GreenProduct;
import com.example.greenproject.service.GreenProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
// 引入CrossOrigin注解
@CrossOrigin
@RestController

@RequestMapping("/api/green-products")
public class GreenProductController {
    @Autowired
    private GreenProductService greenProductService;

    @GetMapping
    public List<GreenProduct> getAllGreenProducts() {
        return greenProductService.getAllGreenProducts();
    }

    @GetMapping("/{id}")
    public GreenProduct getGreenProductById(@PathVariable Long id) {
        return greenProductService.getGreenProductById(id);
    }

    @GetMapping("/search")
    public List<GreenProduct> searchGreenProducts(@RequestParam String search) {
        return greenProductService.searchGreenProducts(search);
    }
}