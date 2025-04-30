package com.example.greenproject.repository;

import com.example.greenproject.entity.ReservationOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationOrderRepository extends JpaRepository<ReservationOrder, Integer> {
    List<ReservationOrder> findByStatus(String status);
}