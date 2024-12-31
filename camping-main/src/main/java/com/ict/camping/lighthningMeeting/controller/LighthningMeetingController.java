// Controller
package com.ict.camping.lighthningMeeting.controller;

import com.ict.camping.lighthningMeeting.service.LighthningMeetingService;
import com.ict.camping.lighthningMeeting.vo.LighthningMeetingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class LighthningMeetingController {

    @Autowired
    private LighthningMeetingService meetingService;

    // Test API
    @GetMapping("/test")
    public ResponseEntity<?> testMeeting() {
        try {
            return ResponseEntity.ok("서버가 정상적으로 작동합니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("테스트 실패: " + e.getMessage());
        }
    }

    // Get all meetings
    @GetMapping
    public ResponseEntity<?> getAllMeetings() {
        System.out.println("==== getAllMeetings() called ====");
        try {
            List<LighthningMeetingVO> meetings = meetingService.getAllMeetings();
            if (meetings.isEmpty()) {
                
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(meetings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류 발생: " + e.getMessage());
        }
    }

    // Get a meeting by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMeetingById(@PathVariable("id") int id) {
        System.out.println("Received ID in Controller: " + id); // 로그 추가
        try {
            LighthningMeetingVO meeting = meetingService.getMeetingById(id);
            if (meeting == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 모임이 존재하지 않습니다.");
            }
            return ResponseEntity.ok(meeting);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류 발생: " + e.getMessage());
        }
    }

    // Create a new meeting
    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody LighthningMeetingVO meetingVO) {
        try {
            meetingService.createMeeting(meetingVO);
            return ResponseEntity.ok(Collections.singletonMap("message", "모임이 성공적으로 생성되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("모임 생성 실패: " + e.getMessage());
        }
    }

    // Update a meeting
@PutMapping("/{id}")
public ResponseEntity<?> updateMeeting(@PathVariable("id") int id, @RequestBody LighthningMeetingVO meetingVO) {
    try {
        meetingVO.setPost_idx(id);
        meetingService.updateMeeting(meetingVO);
        return ResponseEntity.ok(Collections.singletonMap("message", "모임이 성공적으로 수정되었습니다."));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("모임 수정 실패: " + e.getMessage());
    }
}

    // Delete a meeting
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteMeeting(@PathVariable("id") int id) {
    try {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "모임이 성공적으로 삭제되었습니다."));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("모임 삭제 실패: " + e.getMessage());
    }
}
}
