package com.ict.camping.admin.notices.service;

import java.util.List;

import com.ict.camping.admin.notices.vo.ANoticeVO;

public interface ANoticeService {
    // 리스트
    List<ANoticeVO> getAllNotices();
    // 디테일
    ANoticeVO getNoticeDetail(String notice_idx);
    // // answer 디테일
    // AInquiryAnswerVO getInquiryAnswer(String inquiry_idx);
    // // 삭제
    // boolean deleteAdmin(String admin_idx);
    // 삽입
    int insertNotice(ANoticeVO noticeVO);
    // // 중복확인
    // boolean isAnswerOk(String inquiry_idx);
    // // 답변 수정
    // int updateAnswer(AInquiryAnswerVO answerVO);
} 
