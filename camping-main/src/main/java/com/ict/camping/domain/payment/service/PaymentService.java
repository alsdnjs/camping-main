package com.ict.camping.domain.payment.service;

import com.ict.camping.domain.payment.vo.PaymentVO;

public interface PaymentService {
    int insertPayment(PaymentVO paymentVO); // 데이터 삽입 메서드
}