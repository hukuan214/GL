package com.example.greenproject.repository;

import com.example.greenproject.entity.EnvironmentalEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentalEventRepository extends JpaRepository<EnvironmentalEvent, Integer> {
}