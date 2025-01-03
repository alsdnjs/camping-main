package com.ict.camping.domain.myPage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.camping.common.util.JwtUtil;
import com.ict.camping.domain.auth.vo.DataVO;
import com.ict.camping.domain.myPage.service.MyPageService;
import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.InquiryVO;
import com.ict.camping.domain.myPage.vo.MyReviewVO;
import com.ict.camping.domain.myPage.vo.UsageHistoryVO;
import com.ict.camping.domain.users.service.UsersService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/myPage")
public class MyPageController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private UsersService usersService;

      // 내가 찜한 캠핑사이트 가져오기
    @GetMapping("/getMyFavoriteCampingSites")
    public DataVO getMyFavoriteCampingSites(@RequestHeader("Authorization") String authorizationHeader) {
        DataVO dataVO = new DataVO();
        try {
            // 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);

            // 내가 찜한 캠핑장 내용 가져오기(service에서 id로 contentId가져오고 그걸로 cvo 가져옴)
            List<CampingSiteVO> cvo = myPageService.getMyFavoriteCampingSites(userId);
            System.out.println(cvo);
            if(cvo != null){
                dataVO.setData(cvo);
            } else{
                dataVO.setData(null);
            }
            dataVO.setSuccess(true);
            
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("error : " + e );
        }

        return dataVO;
    }

    @GetMapping("/deleteMyCampingSite")
    public DataVO deleteMyCampingSites(
        @RequestParam("contentId") String contentId, 
        @RequestHeader("Authorization") String authorizationHeader) {

        DataVO dataVO = new DataVO();
        try {
            // 토근에서서 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);
            // 사용자 IDX 가져오기
            String user_idx = usersService.getUserIdxById(userId);
            
            System.out.println("유저 idx : " + user_idx);
            System.out.println("캠핑장 id : " + contentId);

            int result = myPageService.deleteMyCampingSite(user_idx, contentId);

            System.out.println(result);

            if(result > 0){
                dataVO.setSuccess(true);
            } else {
                dataVO.setSuccess(false);
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
        }

        return dataVO;
    }

    @GetMapping("/getUsageHistory")
    public DataVO getUsageHistory(@RequestHeader("Authorization") String authorizationHeader) {
        DataVO dataVO = new DataVO();
        try {
            // 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);
            // 사용자 IDX 가져오기
            String user_idx = usersService.getUserIdxById(userId);

            List<UsageHistoryVO> list = myPageService.getUsageHistory(user_idx);
            System.out.println(list);
            dataVO.setData(list);
            dataVO.setSuccess(true);

        } catch (Exception e) {
            dataVO.setSuccess(false);
        }
        return dataVO;
    }

    // 1대1문의 리스트 가져오기
    @GetMapping("/getInquiryHistory")
    public DataVO getInquiryHistory(@RequestHeader("Authorization") String authorizationHeader) {
        DataVO dataVO = new DataVO();
        try {
            // 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);
            // 사용자 IDX 가져오기
            String user_idx = usersService.getUserIdxById(userId);

            List<InquiryVO> list = myPageService.getMyInquiryHistory(user_idx);
            System.out.println(list);
            dataVO.setData(list);
            dataVO.setSuccess(true);

        } catch (Exception e) {
            dataVO.setSuccess(false);
        }
        return dataVO;
    }

    @GetMapping("/getMyReviews")
    public DataVO getMyReviews(@RequestHeader("Authorization") String authorizationHeader) {
        DataVO dataVO = new DataVO();
        try {
            // 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);
            // 사용자 IDX 가져오기
            String user_idx = usersService.getUserIdxById(userId);

            List<MyReviewVO> list = myPageService.getMyReviews(user_idx);
            System.out.println(list);
            dataVO.setData(list);
            dataVO.setSuccess(true);

        } catch (Exception e) {
            dataVO.setSuccess(false);
        }
        return dataVO;
    }
    
    

    // @GetMapping("/getCampingLikesCount")
    // public String getCampingLikesCount(@RequestParam String param) {
        
    // }
    
    

    public String getIdFromToken(String authorizationHeader, DataVO dataVO){
        // 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            dataVO.setSuccess(false);
            dataVO.setMessage("유효하지 않은 토큰입니다.");
            return "";
        }
        // 사용자 ID 추출
        String userId = jwtUtil.getUserIdFromToken(token);
        System.out.println("유저 아이디 : "+  userId);
        return userId;
    }
}