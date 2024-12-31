package com.ict.camping.domain.reqularMeeting.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ict.camping.domain.reqularMeeting.service.ReqularMeetingService;
import com.ict.camping.domain.reqularMeeting.vo.HashtagVO;
import com.ict.camping.domain.reqularMeeting.vo.ReqularMeetingVO;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/regular-meetings")
public class ReqularMeetingController {

    @Autowired
    private ReqularMeetingService reqularMeetingService;

    // 1) 정규 모임 생성
    // @PostMapping
    // public Map<String, Object> createMeeting(@RequestBody ReqularMeetingVO
    // meeting) {
    // // @RequestBody로 받은 JSON 데이터를 VO에 매핑
    // int meetingId = reqularMeetingService.createMeeting(meeting);

    // // 만약 해시태그들을 같이 넘긴다면, 그 해시태그들에 대해 insertMeetingHashtags를 진행
    // // (이 로직은 필요한 형태로 구현)
    // for (String tag : ((ReqularMeetingVO) meeting).getHashtagsList()) {
    // int hashId = reqularMeetingService.findOrCreateHashtag(tag);
    // reqularMeetingService.insertMeetingHashtags(meetingId, hashId);
    // }

    // return Map.of("success", true, "meeting_idx", meetingId);
    // }

    // 2) 전체 목록 조회
    @GetMapping("")
    public ResponseEntity<?> getAllMeetings(@RequestParam(value = "user_idx", required = false, defaultValue = "0") int userIdx) {
        try {
            // user_idx가 0이면 비로그인 유저로 처리
            List<Map<String, Object>> meetings = reqularMeetingService.selectAllMeetings(userIdx);
            List<HashtagVO> hashtags = reqularMeetingService.selectAllHashtags();
    
            Map<String, Object> response = new HashMap<>();
            response.put("meetings", meetings);
            response.put("hashtags", hashtags);
    
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching meetings");
        }
    }
    


    // 3) 특정 모임 상세조회
    @GetMapping("/{meetingId}")
    public ReqularMeetingVO getMeetingById(@PathVariable("meetingId") int meetingId) {
        return reqularMeetingService.selectMeetingById(meetingId);
    }

    // 특정 모임마다 가입 멤버
    @GetMapping("/{meetingId}/members")
public List<Map<String,Object>> getMeetingMembersProfile(@PathVariable int meetingId) {
    return reqularMeetingService.getMeetingMembersProfile(meetingId);
}


    // 4) 좋아요
    @PostMapping("/{meetingId}/favorite")
    public Map<String, Object> toggleFavorite(
            @PathVariable int meetingId,
            @RequestParam("user_idx") int userIdx) {
        try {
            boolean isFavorite = reqularMeetingService.toggleFavorite(userIdx, meetingId);
            return Map.of("success", true, "favorite", isFavorite);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            return Map.of("success", false, "error", e.getMessage());
        }
    }

    // 1) 이미지 파일 업로드 + 모임생성
    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createMeeting(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("region") String region,
            @RequestParam("subregion") String subregion,
            @RequestParam("personnel") int personnel,
            @RequestParam("hashtags") String hashtags, // CSV
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 1) 파일
            // String fileName = null;
            // if (file != null && !file.isEmpty()) {
            // fileName = file.getOriginalFilename();
            // Path uploadDir = Paths.get("uploads/");
            // if (!Files.exists(uploadDir)) {
            // Files.createDirectories(uploadDir);
            // }
            // Path filePath = uploadDir.resolve(fileName);
            // Files.copy(file.getInputStream(), filePath,
            // StandardCopyOption.REPLACE_EXISTING);
            // }
            
            String originalFilename = file.getOriginalFilename();
            String ext = "";
            if (originalFilename.lastIndexOf(".") != -1) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFileName = System.currentTimeMillis() + ext; // 예: 1694492012345.gif

            Path uploadDir = Paths.get("uploads/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(storedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 2) 모임 데이터 저장
            ReqularMeetingVO meeting = new ReqularMeetingVO();
            meeting.setName(name);
            meeting.setDescription(description);
            meeting.setRegion(region);
            meeting.setSubregion(subregion);
            meeting.setPersonnel(personnel);

            // DB에는 storedFileName 저장
            meeting.setProfile_image(storedFileName);

            // 파일명이 null이 아니면 DB에 그 파일명을 저장 (없으면 )
            // meeting.setProfile_image(fileName != null ? fileName : "");

            // leader_idx는 테스트라서 1로
            meeting.setLeader_idx(1);

            int meetingIdx = reqularMeetingService.createMeeting(meeting, null);

            // 3) 해시태그 연결
            // 예: "#글램핑, #산" -> [ "#글램핑", " #산" ]
            for (String hashtag : hashtags.split(",")) {
                String trimTag = hashtag.trim();
                Integer hashtagIdx = reqularMeetingService.findHashtagByName(trimTag);
                if (hashtagIdx != null) {
                    // DB에 존재할 때만 연결
                    reqularMeetingService.insertMeetingHashtags(meetingIdx, hashtagIdx);
                }
            }
            

            return ResponseEntity.ok("Regular meeting created successfully!");
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file upload");
        } catch (Exception e) {
            log.error("Error creating meeting", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating meeting");
        }
    }

}
