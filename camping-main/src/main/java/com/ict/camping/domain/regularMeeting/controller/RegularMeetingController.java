package com.ict.camping.domain.regularMeeting.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;
import com.ict.camping.domain.regularMeeting.service.RegularMeetingService;
import com.ict.camping.domain.regularMeeting.vo.HashtagVO;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingVO;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/regular-meetings")
public class RegularMeetingController {

    @Autowired
    private RegularMeetingService regularMeetingService;

    public RegularMeetingController(RegularMeetingService regularMeetingService) {
        this.regularMeetingService = regularMeetingService;
    }

    // 1) 이미지 파일 업로드 + 모임생성
    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createMeeting(
            @RequestParam("user_idx") int userIdx,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("region") String region,
            @RequestParam("subregion") String subregion,
            @RequestParam("personnel") int personnel,
            @RequestParam("hashtags") String hashtags, // CSV
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 1) 파일 업로드
            String storedFileName = "";
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String ext = "";
                if (originalFilename.lastIndexOf(".") != -1) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                storedFileName = System.currentTimeMillis() + ext; // 예: 1694492012345.gif

                Path uploadDir = Paths.get("src/main/resources/static/upload/");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path filePath = uploadDir.resolve(storedFileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 2) 모임 데이터 저장
            RegularMeetingVO meeting = new RegularMeetingVO();
            meeting.setName(name);
            meeting.setDescription(description);
            meeting.setRegion(region);
            meeting.setSubregion(subregion);
            meeting.setPersonnel(personnel);
            meeting.setProfile_image(storedFileName);
            meeting.setLeader_idx(userIdx);

            // 3) 해시태그 리스트로 변환
            List<String> hashtagList = Arrays.stream(hashtags.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            // 4) 서비스 호출하여 모임 생성 및 해시태그 연결
            int meetingIdx = regularMeetingService.createMeeting(meeting, hashtagList);

            return ResponseEntity.ok("Regular meeting created successfully!");
        } catch (java.io.IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file upload");
        } catch (Exception e) {
            log.error("Error creating meeting", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating meeting");
        }
    }

    // 2) 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllMeetings(
            @RequestParam(value = "user_idx", required = false, defaultValue = "0") int userIdx) {
        try {
            // user_idx가 0이면 비로그인 유저로 처리
            List<RegularMeetingVO> meetings = regularMeetingService.getAllMeetings(userIdx);
            log.info("Meetings: {}", meetings);

            List<HashtagVO> hashtags = regularMeetingService.getAllHashtags();

            Map<String, Object> response = new HashMap<>();
            response.put("meetings", meetings);
            response.put("hashtags", hashtags);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching meetings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching meetings");
        }
    }

    // 3) 특정 모임 상세조회
    @GetMapping("/detail/{meetingId}")
    public ResponseEntity<?> getMeetingById(@PathVariable("meetingId") int meetingId,
            @RequestParam("user_idx") int userIdx) {
        try {
            RegularMeetingVO meeting = regularMeetingService.getMeetingById(meetingId, userIdx);
            System.out.println(meeting);
            if (meeting == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting not found");
            }
            return ResponseEntity.ok(meeting);
        } catch (Exception e) {
            log.error("Failed to fetch meeting details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching meeting details");
        }

    }

    // 4) 정규 모임 업데이트
    @PutMapping(value = "/detail/{meetingId}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMeeting(
            @PathVariable("meetingId") int meetingId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("region") String region,
            @RequestParam("subregion") String subregion,
            @RequestParam("personnel") int personnel,
            @RequestParam("user_idx") int userIdx,
            @RequestParam("leader_idx") int leaderIdx,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 파일 업로드 로직
            String storedFileName = "";
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String ext = "";
                if (originalFilename.lastIndexOf(".") != -1) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                storedFileName = System.currentTimeMillis() + ext; // 예: 1694492012345.gif

                Path uploadDir = Paths.get("src/main/resources/static/upload/");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path filePath = uploadDir.resolve(storedFileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 모임 데이터 업데이트
            RegularMeetingVO meeting = new RegularMeetingVO();
            meeting.setMeeting_idx(meetingId);
            meeting.setName(name);
            meeting.setDescription(description);
            meeting.setRegion(region);
            meeting.setSubregion(subregion);
            meeting.setPersonnel(personnel);
            meeting.setLeader_idx(leaderIdx);
            if (!storedFileName.isEmpty()) {
                meeting.setProfile_image(storedFileName);
            }

            regularMeetingService.updateMeeting(meeting);

            return ResponseEntity.ok("Meeting updated successfully");
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file upload");
        } catch (Exception e) {
            log.error("Error updating meeting", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating meeting");
        }
    }

    @DeleteMapping("/detail/{meetingId}")
    public ResponseEntity<String> deleteMeeting(@PathVariable("meetingId") int meetingId,
            @RequestParam("leader_idx") int leaderIdx) {
        try {
            regularMeetingService.deleteMeeting(meetingId, leaderIdx);
            return ResponseEntity.ok("Meeting deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting meeting", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting meeting");
        }
    }

    // 특정 모임마다 가입 멤버 프로필
    @GetMapping("/detail/{meetingId}/members")
    public ResponseEntity<?> getMeetingMembersProfile(@PathVariable("meetingId") int meetingId) {
        try {
            List<Map<String, Object>> members = regularMeetingService.getMeetingMembersProfile(meetingId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            log.error("Error fetching meeting members", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching members");
        }
    }

    // 모입 가입하기
    @PostMapping("/detail/{meetingId}/join")
    public ResponseEntity<String> joinMeeting(@PathVariable("meetingId") int meetingId,
            @RequestParam("user_idx") int userIdx) {
        try {
            regularMeetingService.joinMeeting(meetingId, userIdx);
            return ResponseEntity.ok("Joined meeting successfully");
        } catch (IllegalStateException e) {
            // 이미 가입된 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching meeting join", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching join");
        }
    }

    // 모입 가입 멤버 확인
    @GetMapping("/detail/{meetingId}/ismember")
    public ResponseEntity<Boolean> isMember(@PathVariable("meetingId") int meetingId, @RequestParam("user_idx") int userIdx) {
        try {
            boolean memberStatus = regularMeetingService.isMember(meetingId, userIdx);
            return ResponseEntity.ok(memberStatus);
        } catch (Exception e) {
            log.error("Error checking isMember status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // 모임 탈퇴
    @PostMapping("/detail/{meetingId}/leave")
    public ResponseEntity<String> leaveMeeting(@PathVariable("meetingId") int meetingId, @RequestParam("user_idx") int userIdx) {
        System.out.println("fhkf롸롸롸롸ㅗ라ㅘㅗㅗㄹ롸가입롸롸롸ㅗ라ㅘㅗㅗㄹ롸가입");
        try {
            regularMeetingService.leaveMeeting(meetingId, userIdx);
            return ResponseEntity.ok("모임을 성공적으로 탈퇴했습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("모임 탈퇴 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("모임 탈퇴 중 오류가 발생했습니다.");
        }
    }

    // 좋아요
    @PostMapping("/detail/{meetingId}/favorite")
    public ResponseEntity<?> toggleFavorite(@PathVariable("meetingId") int meetingId,
            @RequestParam("user_idx") int userIdx) {
        try {
            boolean isFavorite = regularMeetingService.toggleFavorite(userIdx, meetingId);
            return ResponseEntity.ok(Map.of("success", true, "favorite", isFavorite));
        } catch (Exception e) {
            log.error("Error toggling favorite", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 5) 해시태그 목록 조회
    @GetMapping("/hashtags")
    public ResponseEntity<?> getAllHashtags() {
        try {
            List<HashtagVO> hashtags = regularMeetingService.getAllHashtags();
            return ResponseEntity.ok(hashtags);
        } catch (Exception e) {
            log.error("Error fetching hashtags", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching hashtags");
        }
    }

    // 수지 관리자 페이지
    @DeleteMapping("/delete/{meetingId}")
    public ResponseEntity<String> admindeleteMeeting(@PathVariable("meetingId") int meetingId
            ) {
        try {
            regularMeetingService.admindeleteMeeting(meetingId);
            return ResponseEntity.ok("Meeting deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting meeting", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting meeting");
        }
    }
}