package com.ict.camping.admin.campground.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.camping.admin.campground.service.ACampgroundService;
import com.ict.camping.admin.campground.vo.ACampgroundVO;


import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/api/campground")
public class ACampgroundController {

    @Autowired
    private ACampgroundService campgroundService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 특정 지역의 캠핑장 목록 조회
    @GetMapping("/sites")
    public List<ACampgroundVO> getCampingSites() {
        List<ACampgroundVO> campingSites = campgroundService.getAllCampingSites();
        return campingSites;
    }

    @GetMapping("/sites/{camp_request_idx}")
    public ACampgroundVO getCampingSite(@PathVariable("camp_request_idx") String camp_request_idx) {
        System.out.println("컨텐트 아이디가 제대로 오는지 보기 위한 sysout : "+ camp_request_idx);
        return campgroundService.findCampingSiteByContentId(camp_request_idx);
    }

    @PostMapping("/sites/insert/data")
    public ResponseEntity<String> postCampingSite(@ModelAttribute("data") ACampgroundVO formData) {
    MultipartFile file = formData.getFile(); 
    try {
        // 파일 처리
        if (file != null && !file.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String f_name = uuid + "_" + file.getOriginalFilename();
            formData.setFirstImageUrl(f_name);
            String path = "D:\\carlisle\\projectno2\\camping-main-main\\camping-main\\src\\main\\resources\\images";
            File uploadDir = new File(path);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            file.transferTo(new File(uploadDir, f_name));
        } else {
            formData.setFirstImageUrl(null);
        }
        // 데이터 삽입
        int result = campgroundService.insertCampingSite(formData);

        if (result == 0) {
            System.err.println("캠핑장 데이터 삽입 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("캠핑장 데이터 삽입 실패");
        }
        System.out.println("캠핑장 데이터 삽입 성공");
        return ResponseEntity.ok("캠핑장 데이터 삽입 성공");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
    }
    
    }

    @PostMapping("sites/insert/answer")
    public ResponseEntity<String> insertAnswer(@RequestParam("camp_request_idx") int campRequestIdx,
                                               @RequestParam("request_answer") String requestAnswer) {
        try {
            campgroundService.updateRequestAnswer(campRequestIdx, requestAnswer);
            return ResponseEntity.ok("답변이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("답변 등록 중 오류 발생: " + e.getMessage());
        }
    }
    


}
