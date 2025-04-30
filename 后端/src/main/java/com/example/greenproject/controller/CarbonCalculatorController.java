package com.example.greenproject.controller;

import com.example.greenproject.entity.CarbonCalculator;
import com.example.greenproject.repository.CarbonCalculatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
// 为了解决 CrossOrigin 无法解析的问题，需要导入 CrossOrigin 注解。
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
@RequestMapping("/api/carbon-calculator")
public class CarbonCalculatorController {
    @Autowired
    private CarbonCalculatorRepository carbonCalculatorRepository;

    @GetMapping("/travel-types")
    public List<CarbonCalculator> getTravelTypes() {
        return carbonCalculatorRepository.findAll();
    }
}