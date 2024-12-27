package com.ict.camping.domain.payment.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.domain.payment.vo.PaymentVO;

@Mapper
public interface PaymentMapper {
   
    int insertPayment(PaymentVO paymentVO); // 데이터 삽입 메서드
}
