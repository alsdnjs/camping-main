package com.ict.camping.domain.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ict.camping.domain.payment.service.PaymentService;
import com.ict.camping.domain.payment.vo.PaymentVO;

@RestController
@RequestMapping("/api/camping")

public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<String> insertPayment(@RequestBody PaymentVO paymentVO) {
        System.out.println("Received Payment Data: " + paymentVO); // 디버깅용 로그

        try {
            int result = paymentService.insertPayment(paymentVO);
            if (result > 0) {
                System.out.println("Payment insertion successful"); // 디버깅용 로그
                return ResponseEntity.ok("Payment inserted successfully");
            } else {
                System.err.println("Payment insertion failed"); // 디버깅용 로그
                return ResponseEntity.status(500).body("Failed to insert payment");
            }
        } catch (Exception e) {
            System.err.println("Error while inserting payment: " + e.getMessage()); // 디버깅용 로그
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}