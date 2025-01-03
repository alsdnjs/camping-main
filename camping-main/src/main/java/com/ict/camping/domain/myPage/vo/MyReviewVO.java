package com.ict.camping.domain.myPage.vo;

import lombok.Data;

@Data
public class MyReviewVO {
    private String review_idx, contentId, user_idx, rating, title, content, file_idx, created_at;
    private String firstImageUrl, facltNm;
    private String file_name, file_path;
}
