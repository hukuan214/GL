package com.example.greenproject.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class CarbonCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String travel_type;
    private BigDecimal carbon_reduction_per_unit;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTravel_type() {
        return travel_type;
    }

    public void setTravel_type(String travel_type) {
        this.travel_type = travel_type;
    }

    public BigDecimal getCarbon_reduction_per_unit() {
        return carbon_reduction_per_unit;
    }

    public void setCarbon_reduction_per_unit(BigDecimal carbon_reduction_per_unit) {
        this.carbon_reduction_per_unit = carbon_reduction_per_unit;
    }
}