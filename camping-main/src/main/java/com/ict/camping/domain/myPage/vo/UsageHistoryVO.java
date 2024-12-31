package com.ict.camping.domain.myPage.vo;

import lombok.Data;

@Data
public class UsageHistoryVO { 
    private String history_idx, user_idx, contentId, action_type, checkin, created_at, payment_amount, checkout;
    private String facltNm;
}

