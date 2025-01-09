package com.ict.camping.domain.myPage.service;

import java.util.List;

import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.FileVO;
import com.ict.camping.domain.myPage.vo.InquiryVO;
import com.ict.camping.domain.myPage.vo.MyReviewVO;
import com.ict.camping.domain.myPage.vo.MyRegularMeetingVO;
import com.ict.camping.domain.myPage.vo.UsageHistoryVO;


public interface MyPageService {
    // 내가 찜한 사이트 id로 조회해서 목록 가져오기
    public List<CampingSiteVO> getMyFavoriteCampingSites(String id);

    public int deleteMyCampingSite(String user_idx, String contentId);

    public List<UsageHistoryVO> getUsageHistory(String user_idx);

    public String getCampingLikesCount(String contentId);

    public List<InquiryVO> getMyInquiryHistory(String user_idx);

    public List<MyReviewVO> getMyReviews(String user_idx);

    public int setFile(FileVO fvo);

    public int insertInquiry(InquiryVO ivo);

    public int updateProfileImage(String user_idx, String avatar_url);

    public int deleteImageFile(String file_name);

    public List<MyRegularMeetingVO> getMyMeetingsList(String user_idx);
    
    public List<MyRegularMeetingVO> getMyLikesMeetings(String user_idx);

    public int toggleLikesDelete(String user_idx, String meeting_idx);
}