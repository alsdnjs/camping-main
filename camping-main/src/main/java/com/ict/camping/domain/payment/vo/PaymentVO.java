package com.ict.camping.domain.payment.vo;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVO {
 
    private int history_idx;         // 히스토리 인덱스
    private int user_idx;            // 사용자 인덱스
    private int contentId;          // 캠핑장 ID
    private String action_type;      // 액션 타입 (예약 또는 이용)
    private String checkin;        // 체크인 날짜
    private int payment_amount;      // 결제 금액
    private String checkout;        // 체크아웃 날짜
}
