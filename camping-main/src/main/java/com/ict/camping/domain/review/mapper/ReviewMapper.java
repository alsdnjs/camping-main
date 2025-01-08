package com.ict.camping.domain.review.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.admin.files.vo.AfileVO;
import com.ict.camping.domain.review.vo.ReviewVO;

@Mapper
public interface ReviewMapper {
    List<ReviewVO> getReviewList(String contentId);
    
    int getReviewUpdate(ReviewVO rvo);

    int getReviewDelete(String review_idx);

    int getReviewWrite(ReviewVO rvo);

    AfileVO getFileDetail(int file_idx);
}
