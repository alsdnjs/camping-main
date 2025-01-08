package com.ict.camping.domain.review.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.camping.admin.files.service.AfileService;
import com.ict.camping.admin.files.vo.AfileVO;
import com.ict.camping.admin.notices.vo.ANoticeVO;
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

    @Autowired
    private AfileService afileService;
    // 절대 경로 설정
    private static final String FILE_DIRECTORY = "C:/Users/5/Desktop/camping-main/camping-main/src/main/resources/static/upload/";

    // 파일 저장 처리
    private String saveFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename(); // 고유 파일 이름 생성
        File dest = new File(FILE_DIRECTORY, fileName); // 디렉토리와 파일 이름 결합

        // 디렉토리 생성 확인 및 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        // 파일 저장
        file.transferTo(dest);
        return dest.getAbsolutePath(); // 저장된 파일의 절대 경로 반환
    }
    @PostMapping("/review/upload")
    public ResponseEntity<Map<String, Object>> uploadReviewFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String filePath = saveFile(file);

            AfileVO fileVO = new AfileVO();
            fileVO.setFile_name(new File(filePath).getName());
            fileVO.setFile_path(filePath);
            fileVO.setFile_size((int) file.getSize());
            fileVO.setFile_type("image");
            fileVO.setType("리뷰");

            int fileIdx = afileService.insertFile(fileVO);

            response.put("file_idx", fileIdx);
            response.put("file_path", filePath);
            response.put("message", "파일 업로드 성공");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "파일 업로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/review/update-file/{file_idx}")
    public ResponseEntity<Map<String, Object>> updateFile(
            @PathVariable("file_idx") int fileIdx,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String filePath = saveFile(file);
            if (file != null) {
                AfileVO oldFileVO = reviewService.getFileDetail(fileIdx);
                String oldFilePath = oldFileVO.getFile_path();
                if (oldFilePath != null) {
                    File oldFile = new File(oldFilePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                AfileVO fileVO = new AfileVO();
                fileVO.setFile_idx(fileIdx);
                fileVO.setFile_name(new File(filePath).getName());
                fileVO.setFile_path(filePath);
                fileVO.setFile_size((int) file.getSize());
                fileVO.setFile_type("image");
                fileVO.setType("리뷰");

                int updateResult = afileService.updateFile(fileVO);
                if (updateResult == 0) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("message", "파일 정보 업데이트에 실패했습니다."));
                }

                response.put("file_idx", fileIdx);
                response.put("file_path", filePath);
                response.put("message", "파일 수정 성공 및 공지사항 파일 정보 업데이트 완료");
            } else {
                response.put("message", "새로운 파일이 업로드되지 않았습니다.");
            }

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "파일 수정 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/list/{contentId}")
    public List<ReviewVO> getReviewList(@PathVariable("contentId") String contentId) {
        List<ReviewVO> reviewList = reviewService.getReviewList(contentId);
        return reviewList;
    }
    
    @PostMapping("/write")
    public DataVO getReviewWrite(@ModelAttribute("data") ReviewVO rvo) {
        DataVO dataVO = new DataVO();
        try {
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
 