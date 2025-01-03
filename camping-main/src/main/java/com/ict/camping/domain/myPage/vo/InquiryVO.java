package com.ict.camping.domain.myPage.vo;

import lombok.Data;

@Data
public class InquiryVO {
    private String inquiry_idx, user_idx, subject, content, created_at, file_idx;
    private String answer_idx, answer, admin_idx, answer_created_at;
    private String file_path, file_name, file_type;
}
