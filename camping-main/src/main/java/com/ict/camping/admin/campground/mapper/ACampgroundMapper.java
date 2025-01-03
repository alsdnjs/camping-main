package com.ict.camping.admin.campground.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ict.camping.admin.campground.vo.ACampgroundVO;
@Mapper
public interface ACampgroundMapper {
    // 모든 캠핑장 데이터를 조회하는 메서드
    List<ACampgroundVO> findAllCampgroundNotice();

    // contentId를 사용하여 특정 캠핑장 정보를 조회하는 메서드
    ACampgroundVO findCampgroundNoticeById(String contentId);

    // 캠핑장 데이터를 삽입
    int insertCampgroundNotice(ACampgroundVO mapVO);

    void updateRequestAnswer(@Param("camp_request_idx") int campRequestIdx, 
                             @Param("request_answer") String requestAnswer);
}