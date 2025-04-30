package com.example.greenproject.repository;

import com.example.greenproject.entity.CarbonCalculator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarbonCalculatorRepository extends JpaRepository<CarbonCalculator, Integer> {
}