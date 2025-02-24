package com.ict.camping.domain.myPage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.domain.myPage.mapper.MyPageMapper;
import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.FileVO;
import com.ict.camping.domain.myPage.vo.InquiryVO;
import com.ict.camping.domain.myPage.vo.MyRegularMeetingVO;
import com.ict.camping.domain.myPage.vo.MyReviewVO;
import com.ict.camping.domain.myPage.vo.MyRegularMeetingVO;
import com.ict.camping.domain.myPage.vo.UsageHistoryVO;
import com.ict.camping.domain.users.mapper.UsersMapper;

@Service
public class MyPageServiceImpl implements MyPageService{

    @Autowired
    private MyPageMapper myPageMapper;
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public List<CampingSiteVO> getMyFavoriteCampingSites(String id) {
        String user_idx = usersMapper.getUserIdxById(id);
        System.out.println("user_idx : " + user_idx);

        List<String> contentIds = myPageMapper.getMyFavoriteCampingSites(user_idx);
        System.out.println(contentIds);

        List<CampingSiteVO> cvo = myPageMapper.getCampingSitesById(contentIds);
        System.out.println(cvo);

        return cvo;
    }

    @Override
    public int deleteMyCampingSite(String user_idx, String contentId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_idx", user_idx);
        params.put("contentId", contentId);
        return myPageMapper.deleteMyCampingSite(params);
    }

    @Override
    public List<UsageHistoryVO> getUsageHistory(String user_idx) {
        return myPageMapper.getUsageHistory(user_idx);
    }

    @Override
    public String getCampingLikesCount(String contentId) {
        return myPageMapper.getCampingLikesCount(contentId);
    }

    @Override
    public List<InquiryVO> getMyInquiryHistory(String user_idx) {
        return myPageMapper.getMyInquiryHistory(user_idx);
    }

    @Override
    public List<MyReviewVO> getMyReviews(String user_idx) {
        return myPageMapper.getMyReviews(user_idx);
    }

    @Override
    public int setFile(FileVO fvo) {
        return myPageMapper.setFile(fvo);
    }

    @Override
    public int insertInquiry(InquiryVO ivo) {
        return myPageMapper.insertInquiry(ivo);
    }

    @Override
    public int updateProfileImage(String user_idx, String avatar_url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_idx", user_idx);
        params.put("avatar_url", avatar_url);
        return myPageMapper.updateProfileImage(params);
    }

    @Override
    public int deleteImageFile(String file_name) {
        return myPageMapper.deleteImageFile(file_name);
    }

    @Override
    public List<MyRegularMeetingVO> getMyMeetingsList(String user_idx) {
        return myPageMapper.getMyMeetingsList(user_idx);
    }

    @Override
    public List<MyRegularMeetingVO> getMyLikesMeetings(String user_idx) {
        return myPageMapper.getMyLikesMeetings(user_idx);
    }

    @Override
    public int toggleLikesDelete(String user_idx, String meeting_idx) {
        Map<String, String> params = new HashMap<>();
        params.put("user_idx", user_idx);
        params.put("meeting_idx", meeting_idx);
        return myPageMapper.toggleLikesDelete(params);
    }
}