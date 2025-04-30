package com.example.greenproject.controller;

import com.example.greenproject.entity.ReservationOrder;
import com.example.greenproject.repository.ReservationOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reservation-orders")
@CrossOrigin
public class AdminReservationOrderController {
    
    @Autowired
    private ReservationOrderRepository repository;

    @GetMapping
    public List<ReservationOrder> getAllOrders() {
        return repository.findAll();
    }

    @PutMapping("/{id}/approve")
    public ReservationOrder approveOrder(@PathVariable Integer id) {
        ReservationOrder order = repository.findById(id).orElseThrow();
        order.setStatus("已通过");
        return repository.save(order);
    }

    @PutMapping("/{id}/reject")
    public ReservationOrder rejectOrder(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        ReservationOrder order = repository.findById(id).orElseThrow();
        order.setStatus("未通过");
        order.setRejection_reason(request.get("reason")); // 从JSON对象中获取reason字段
        return repository.save(order);
    }
}