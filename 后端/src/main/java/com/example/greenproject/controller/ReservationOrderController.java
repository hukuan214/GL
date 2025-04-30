package com.example.greenproject.controller;

import com.example.greenproject.entity.ReservationOrder;
import com.example.greenproject.repository.ReservationOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.List; // 添加List类型的导入语句

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ReservationOrderController {
    @Autowired
    private ReservationOrderRepository reservationOrderRepository;

    @PostMapping("/reservation-orders")
    public ReservationOrder createReservationOrder(@RequestBody ReservationOrder reservationOrder) {
        // 验证手机号格式
        if (reservationOrder.getPhone_number() == null || 
            !Pattern.matches("^1[3-9]\\d{9}$", reservationOrder.getPhone_number())) {
            throw new IllegalArgumentException("请输入有效的手机号码");
        }
        
        reservationOrder.setStatus("待审核");
        reservationOrder.setOrder_date(new Date());
        return reservationOrderRepository.save(reservationOrder);
    }

    @GetMapping("/rejected-orders")
    public List<ReservationOrder> getRejectedOrders() {
        return reservationOrderRepository.findByStatus("未通过");
    }
}