package com.ict.camping.domain.review.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewVO {
    private String review_idx, contentId, username, rating, created_at, user_idx, title ,content, filename;
    private MultipartFile file;
}
