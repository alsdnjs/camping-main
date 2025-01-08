package com.ict.camping.domain.myPage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.FileVO;
import com.ict.camping.domain.myPage.vo.InquiryVO;
import com.ict.camping.domain.myPage.vo.MyReviewVO;
import com.ict.camping.domain.myPage.vo.UsageHistoryVO;


@Mapper
public interface MyPageMapper {
    List<String> getMyFavoriteCampingSites(String user_idx);
    List<CampingSiteVO> getCampingSitesById(List<String> contentIds);
    
    int deleteMyCampingSite(Map<String, String> map);

    List<UsageHistoryVO> getUsageHistory(String user_idx);

    String getCampingLikesCount(String contentId);

    List<InquiryVO> getMyInquiryHistory(String user_idx);

    List<MyReviewVO> getMyReviews(String user_idx);

    int setFile(FileVO fvo);

    int insertInquiry(InquiryVO ivo);

    int updateProfileImage(Map<String, String> map);

    int deleteImageFile(String file_name);
}