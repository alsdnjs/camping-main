package com.ict.camping.admin.notices.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ANoticeVO {
    private int notice_idx;
    private String notice_subject;
    private String notice_content;
    private int image_idx;
    private int admin_idx;
    private String created_at;
    private String id;
}
