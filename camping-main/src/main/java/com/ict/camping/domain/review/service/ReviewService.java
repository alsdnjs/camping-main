package com.ict.camping.domain.review.service;

import java.util.List;

import com.ict.camping.domain.review.vo.ReviewVO;

public interface ReviewService {
    List<ReviewVO> getReviewList(String contentId);

    //ReviewVO getReviewById(String review_idx);

    int getReviewUpdate(ReviewVO rvo);

    int getReviewDelete(String review_idx);

    int getReviewWrite(ReviewVO rvo);
}
