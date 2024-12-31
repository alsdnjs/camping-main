package com.ict.camping.domain.review.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.camping.domain.auth.vo.DataVO;
import com.ict.camping.domain.review.service.ReviewService;
import com.ict.camping.domain.review.vo.ReviewVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;



@Slf4j
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/list/{contentId}")
    public List<ReviewVO> getReviewList(@PathVariable("contentId") String contentId) {
        List<ReviewVO> reviewList = reviewService.getReviewList(contentId);
        return reviewList;
    }
    
    @PostMapping("/write")
    public DataVO getReviewWrite(@ModelAttribute("data") ReviewVO rvo) {
        DataVO dataVO = new DataVO();
        try {
            MultipartFile file = rvo.getFile();
            if (file == null || file.isEmpty()) {
                rvo.setFilename(null);  // 파일 없으면 filename 빈 문자열로 설정
            } else {
                UUID uuid = UUID.randomUUID();
                String f_name = uuid.toString() + "_" + file.getOriginalFilename();
                rvo.setFilename(f_name);

                // 프로젝트 내부의 resources/static/upload 경로
                String path = "D:\\upload";
                File uploadDir = new File(path);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                file.transferTo(new File(uploadDir, f_name));
            }
            // 게스트북 쓰기
            int result = reviewService.getReviewWrite(rvo);
            if (result == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("리뷰 쓰기 실패");
                return dataVO;
            }
            dataVO.setSuccess(true);
            dataVO.setMessage("리뷰 쓰기 성공");

        } catch (Exception e) {
            log.info("Exception : " + e);
            dataVO.setSuccess(false);
            dataVO.setMessage("작성 중 오류가 발생했습니다.");
        }
        return dataVO;
    }

    @GetMapping("/delete/{review_idx}")
    public DataVO getReviewDelete(@PathVariable("review_idx") String review_idx) {
        DataVO dataVO = new DataVO();
        try {
            int deleteReview = reviewService.getReviewDelete(review_idx);
            if (deleteReview == 0) {
                dataVO.setSuccess(false);
                dataVO.setMessage("삭제 실패");
            } else {
                dataVO.setMessage("삭제 성공");
                dataVO.setSuccess(true);
            }
        } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("오류 발생");
        }
        return dataVO; 
    }
    @PostMapping("/update/{review_idx}")
    public DataVO getReviewUpdate(@PathVariable("review_idx") String review_idx , @ModelAttribute("data") ReviewVO rvo, Authentication authentication) {
        DataVO dataVO = new DataVO();
        rvo.setReview_idx(review_idx);
        try {
            MultipartFile file = rvo.getFile();
            if (file == null || file.isEmpty()) {
                rvo.setFilename(null);  // 파일 없으면 filename 빈 문자열로 설정
            } else {
                UUID uuid = UUID.randomUUID();
                String f_name = uuid.toString() + "_" + file.getOriginalFilename();
                rvo.setFilename(f_name);

                // 프로젝트 내부의 resources/static/upload 경로
                String path = "D:\\upload";
                File uploadDir = new File(path);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                file.transferTo(new File(uploadDir, f_name));
            }
            int result = reviewService.getReviewUpdate(rvo);
            dataVO.setSuccess(true);
            dataVO.setMessage("리뷰 수정이 완료되었습니다");

        } catch (Exception e) {
            log.info("Exception : " + e);
            dataVO.setSuccess(false);
            dataVO.setMessage("수정 중 오류가 발생했습니다.");
        }
        return dataVO;
    }
}
 