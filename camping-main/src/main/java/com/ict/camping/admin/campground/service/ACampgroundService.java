package com.ict.camping.admin.campground.service;

import java.util.List;

import com.ict.camping.admin.campground.vo.ACampgroundVO;

public interface ACampgroundService {
    // 모든 캠핑장 데이터를 조회하는 메서드
    List<ACampgroundVO> getAllCampingSites();
    // 디테일
    ACampgroundVO findCampingSiteByContentId(String contentId);

    int insertCampingSite(ACampgroundVO mapVO);

    void updateRequestAnswer(int campRequestIdx, String requestAnswer);
}