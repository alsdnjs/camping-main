package com.ict.camping.domain.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.domain.payment.mapper.PaymentMapper;
import com.ict.camping.domain.payment.vo.PaymentVO;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public int insertPayment(PaymentVO paymentVO) {
        
        return paymentMapper.insertPayment(paymentVO); // Mapper 호출
    }
       
    }



 
    
    
