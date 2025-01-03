package com.ict.camping.admin.notices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.camping.admin.notices.service.ANoticeService;
import com.ict.camping.admin.notices.vo.ANoticeVO;
import com.ict.camping.admin.notices.vo.APopupVO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/notice")
public class ANoticeController {

    @Autowired
    private ANoticeService noticeService;

    // 공지사항 불러오기
    @GetMapping("/notices")
    public List<ANoticeVO> getAllNotices() {
        List<ANoticeVO> allNotices = noticeService.getAllNotices();
        return allNotices;
    }

    // 팝업 불러오기
    @GetMapping("/popups")
    public List<APopupVO> getAllPopups() {
        List<APopupVO> allPopups = noticeService.getAllPopups();
        return allPopups;
    }
    

    @GetMapping("/notices/{notice_idx}")
    public ANoticeVO getNoticeDetail(@PathVariable("notice_idx") String notice_idx) {
        return noticeService.getNoticeDetail(notice_idx);
    }

    @DeleteMapping("/notices/delete/{notice_idx}")
    public ResponseEntity<String> deleteNotice(@PathVariable("notice_idx") String notice_idx){
        try {
            // 댓글 삭제 서비스 호출
            boolean isDeleted = noticeService.deleteNotice(notice_idx);
            if (isDeleted) {
                return ResponseEntity.ok("공지사항이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제하려는 공지사항이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 삭제 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/popup/delete/{popup_idx}")
    public ResponseEntity<String> deletePopup(@PathVariable("popup_idx") String popup_idx){
        try {
            // 댓글 삭제 서비스 호출
            boolean isDeleted = noticeService.deletePopup(popup_idx);
            if (isDeleted) {
                return ResponseEntity.ok("팝업이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제하려는 팝업이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("팝업 삭제 중 오류가 발생했습니다.");
        }
    }

    
    @PostMapping("/notices/update/{notice_idx}")
    public ResponseEntity<String> updateNotice(@PathVariable("notice_idx") String notice_idx,
                                            @ModelAttribute ANoticeVO formData) {
    ANoticeVO existingNotice = noticeService.getNoticeDetail(notice_idx);
    if (existingNotice == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 공지사항을 찾을 수 없습니다.");
    }
    // 기존 데이터를 유지하며 새로운 데이터로 업데이트
    existingNotice.setNotice_subject(formData.getNotice_subject() != null ? formData.getNotice_subject() : existingNotice.getNotice_subject());
    existingNotice.setNotice_content(formData.getNotice_content() != null ? formData.getNotice_content() : existingNotice.getNotice_content());
    existingNotice.setAdmin_idx(formData.getAdmin_idx() != 0 ? formData.getAdmin_idx() : existingNotice.getAdmin_idx());
    // 업데이트 실행
    int result = noticeService.updateNotice(existingNotice);
    if (result == 0) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 업데이트에 실패했습니다.");
    }

    return ResponseEntity.ok("공지사항이 성공적으로 업데이트되었습니다.");
    }

    @PostMapping("/popup/update-visibility/{popup_idx}/{is_hidden}")
    public ResponseEntity<String> updatePopupVisibility(@PathVariable("popup_idx") String popup_idx,
                                                         @PathVariable("is_hidden") String is_hidden) {
        try {
            // APopupVO 객체 생성 및 값 설정
            APopupVO popupVO = new APopupVO();
            int popupIdx = Integer.valueOf(popup_idx);
            popupVO.setPopup_idx(popupIdx);
            popupVO.setIs_hidden(is_hidden);
    
            // 업데이트 실행
            int result = noticeService.updatePopupVisibility(popupVO);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상태 업데이트에 실패했습니다.");
            }
            return ResponseEntity.ok("상태가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/notices/insert")
    public int insertNotice(@ModelAttribute("data") ANoticeVO formData) {
        int result = noticeService.insertNotice(formData);
        if (result == 0) {
            System.out.println("데이터 삽입 실패");
            return 0;
        }
        System.out.println("데이터 삽입 성공");
        return result;
    }

    
    @PostMapping("/popup/insert")
    public int insertPopup(@ModelAttribute("data") APopupVO formData) {
        int result = noticeService.insertPopup(formData);
        if (result == 0) {
            System.out.println("데이터 삽입 실패");
            return 0;
        }
        System.out.println("데이터 삽입 성공");
        return result;
    }


    
}
