package com.ict.camping.domain.myPage.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.camping.common.util.FileUtils;
import com.ict.camping.common.util.JwtUtil;
import com.ict.camping.domain.auth.vo.DataVO;
import com.ict.camping.domain.myPage.service.MyPageService;
import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.FileVO;
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
    private final String UPLOAD_DIR = "D:/CampingProject/camping/src/main/resources/static/upload/";
    // FileUtils 클래스를 인스턴스화하여 사용
    private final FileUtils fileUtils = new FileUtils();

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
    
    @PostMapping("/sendInquiry")
    public DataVO submitInquiry(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam("subject") String subject,
        @RequestParam("content") String content,
        @RequestParam(value = "file", required = false) MultipartFile file) {
            
        DataVO dataVO = new DataVO();
        FileVO fvo = new FileVO();
        InquiryVO ivo = new InquiryVO();

        // 제목과 내용 처리
        ivo.setContent(content);
        ivo.setSubject(subject);
        System.out.println("제목: " + subject);
        System.out.println("내용: " + content);
        try {
            // 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);
            // 사용자 IDX 가져오기
            String user_idx = usersService.getUserIdxById(userId);
            // VO에 user_idx 저장
            ivo.setUser_idx(user_idx);
        } catch (Exception e) {
            System.out.println("사용자 인증 실패 : " + e);
        }
        
        // 파일 처리 (파일이 있을 경우)
        if (file != null && !file.isEmpty()) {  // 파일이 null이 아니고 비어있지 않을 때만 처리
            try {
                String uploadDir = new File("src/main/resources/static/upload").getAbsolutePath();

                String fileName = fileUtils.saveFile(file, uploadDir);
                
                fvo.setFile_name(fileName);

                // files 테이블에 저장(DB)
                int result = myPageService.setFile(fvo);
                if(result > 0){
                    String file_idx = fvo.getFile_idx();
                    // InquiryVO에 file_idx 저장
                    ivo.setFile_idx(file_idx);
                    // Inquiry 테이블에 VO 저장
                    int result1 = myPageService.insertInquiry(ivo);
                    if(result1 > 0){
                        dataVO.setSuccess(true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
                dataVO.setSuccess(false);
            }
        } else {
            System.out.println("파일이 업로드되지 않았습니다.");
            int result = myPageService.insertInquiry(ivo);
            if(result > 0){
                dataVO.setSuccess(true);
            } else {
                dataVO.setSuccess(false);
            }
        }
        return dataVO;
    }


    // 프로필 사진 업데이트
    @PostMapping("/updateProfileImage")
    public DataVO updateProfileImage(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam("image") MultipartFile file,
        @RequestParam("prevImage") String prevImage) {

        DataVO dataVO = new DataVO();
        FileVO fvo = new FileVO();

        // 기존 이미지 파일 삭제제
        String uploadDir = new File("src/main/resources/static/upload").getAbsolutePath();
        File fileToDelete = new File(UPLOAD_DIR + prevImage);
        try {
                        // 파일 존재 여부 확인 후 삭제
            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                    System.out.println("파일 삭제 성공");
                } else {
                    System.out.println("파일 삭제 실패.");
                }
            } else {
                System.out.println("파일이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            // 사용자 ID 추출
            String userId = getIdFromToken(authorizationHeader, dataVO);
            // 사용자 IDX 가져오기
            String user_idx = usersService.getUserIdxById(userId);
            String fileName = fileUtils.saveFile(file, uploadDir);

            // 파일 정보를 객체에 저장
            System.out.println(fileName);
            fvo.setFile_name(fileName);

            // files 테이블에 저장(DB)
            int result = myPageService.setFile(fvo);
            if(result > 0){
                String file_idx = fvo.getFile_idx();
                // users 테이블에 file_idx 수정
                int result1 = myPageService.updateProfileImage(user_idx, file_idx);
                // DB files 에서 삭제
                myPageService.deleteImageFile(prevImage);
                if(result1 > 0){
                    dataVO.setSuccess(true);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
            dataVO.setSuccess(false);
        }
        return dataVO;
    }
    
    

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