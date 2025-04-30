package com.example.greenproject.controller;

import com.example.greenproject.entity.EnvironmentalEvent;
import com.example.greenproject.repository.EnvironmentalEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
// 为了解决 CrossOrigin 无法解析的问题，需要导入 CrossOrigin 注解。
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class EnvironmentalEventController {
    @Autowired
    private EnvironmentalEventRepository environmentalEventRepository;

    @GetMapping("/environmental-events")  // 保持原样
    public List<EnvironmentalEvent> getEnvironmentalEvents() {
        return environmentalEventRepository.findAll();
    }
}