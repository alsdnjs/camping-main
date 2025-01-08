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
    private String review_idx, contentId, username, rating, created_at, user_idx, title ,content, file_idx;
    
    // join
    private String file_name;
    private String file_path;
    private int file_size;
    private String file_type;
    private String type;
}
