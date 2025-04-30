package com.example.greenproject.controller;

import com.example.greenproject.entity.CarbonCalculator;
import com.example.greenproject.repository.CarbonCalculatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/carbon-calculator")
public class AdminCarbonCalculatorController {
    
    @Autowired
    private CarbonCalculatorRepository repository;

    @GetMapping
    public List<CarbonCalculator> getAllTravelTypes() {
        return repository.findAll();
    }

    @PostMapping
    public CarbonCalculator addTravelType(@RequestBody CarbonCalculator travelType) {
        return repository.save(travelType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarbonCalculator> updateTravelType(
            @PathVariable Integer id, 
            @RequestBody CarbonCalculator travelTypeData) {
        return repository.findById(id)
            .map(travelType -> {
                travelType.setTravel_type(travelTypeData.getTravel_type());
                travelType.setCarbon_reduction_per_unit(travelTypeData.getCarbon_reduction_per_unit());
                CarbonCalculator updated = repository.save(travelType);
                return ResponseEntity.ok(updated);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravelType(@PathVariable Integer id) {
        return repository.findById(id)
            .map(travelType -> {
                repository.delete(travelType);
                return ResponseEntity.ok().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}