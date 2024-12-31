package com.ict.camping.admin.inquiry.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AInquiryAnswerVO {
    private int inquiry_idx;
    private int answer_idx;
    private String answer;
    private int admin_idx;
    private String created_at;
}
