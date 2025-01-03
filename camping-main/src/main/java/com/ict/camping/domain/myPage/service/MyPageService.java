package com.ict.camping.domain.myPage.service;

import java.util.List;

import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.InquiryVO;
import com.ict.camping.domain.myPage.vo.MyReviewVO;
import com.ict.camping.domain.myPage.vo.UsageHistoryVO;


public interface MyPageService {
    // 내가 찜한 사이트 id로 조회해서 목록 가져오기
    public List<CampingSiteVO> getMyFavoriteCampingSites(String id);

    public int deleteMyCampingSite(String user_idx, String contentId);

    public List<UsageHistoryVO> getUsageHistory(String user_idx);

    public String getCampingLikesCount(String contentId);

    public List<InquiryVO> getMyInquiryHistory(String user_idx);

    public List<MyReviewVO> getMyReviews(String user_idx);
}