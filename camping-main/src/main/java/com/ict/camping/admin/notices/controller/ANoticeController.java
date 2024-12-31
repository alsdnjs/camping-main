package com.ict.camping.admin.notices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.camping.admin.inquiry.service.AInquiryService;
import com.ict.camping.admin.inquiry.vo.AInquiryAnswerVO;
import com.ict.camping.admin.inquiry.vo.AInquiryVO;
import com.ict.camping.admin.notices.service.ANoticeService;
import com.ict.camping.admin.notices.vo.ANoticeVO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/api/notice")
public class ANoticeController {

    @Autowired
    private ANoticeService noticeService;

    @GetMapping("/notices")
    public List<ANoticeVO> getAllNotices() {
        List<ANoticeVO> allNotices = noticeService.getAllNotices();
        return allNotices;
    }

    @GetMapping("/notices/{notice_idx}")
    public ANoticeVO getNoticeDetail(@PathVariable("notice_idx") String notice_idx) {
        return noticeService.getNoticeDetail(notice_idx);
    }

    // @GetMapping("/inquiries/answer/{inquiry_idx}")
    // public AInquiryAnswerVO getInquiryAnswer(@PathVariable("inquiry_idx") String inquiry_idx) {
    //     return inquiryService.getInquiryAnswer(inquiry_idx);
    // }

    
    // @PostMapping("/inquiries/answer/update/{inquiry_idx}")
    // public ResponseEntity<String> updateAnswer(@PathVariable("inquiry_idx") String inquiry_idx,
    // @ModelAttribute AInquiryAnswerVO formData) {
    //     AInquiryAnswerVO existingInquiry = inquiryService.getInquiryAnswer(inquiry_idx);
    //     if (existingInquiry == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 inquiry에 대한 답변이 없음");
    //     }
    //     existingInquiry.setAdmin_idx(formData.getAdmin_idx() != 0 ? formData.getAdmin_idx() : existingInquiry.getAdmin_idx());
    //     existingInquiry.setAnswer(formData.getAnswer() != null ? formData.getAnswer() : existingInquiry.getAnswer());

    //     int result = inquiryService.updateAnswer(existingInquiry);
    //     if (result == 0) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("답변 정보 업데이트에 실패했습니다.");
    //     }
    //     return ResponseEntity.ok("답변 정보가 성공적으로 업데이트되었습니다.");
    // }
    

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

    // @GetMapping("/inquiries/check-answer")
    // public boolean checkIdDuplicate(@RequestParam("inquiry_idx") String inquiry_idx) {
    //     boolean isDuplicate = inquiryService.isAnswerOk(inquiry_idx);
    //     if (isDuplicate) {
    //         System.out.println("답변완료");
    //     } else {
    //         System.out.println("답변없음");
    //     }
    //     return isDuplicate;
    // }
    
    


    
}
