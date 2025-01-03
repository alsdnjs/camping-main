package com.ict.camping.admin.notices.service;

import java.util.List;

import com.ict.camping.admin.notices.vo.ANoticeVO;
import com.ict.camping.admin.notices.vo.APopupVO;

public interface ANoticeService {
    // 리스트
    List<ANoticeVO> getAllNotices();
    // 디테일
    ANoticeVO getNoticeDetail(String notice_idx);
    // 팝업 리스트
    List<APopupVO> getAllPopups();
    // 삭제
    boolean deleteNotice(String notice_idx);
    // 삽입
    int insertNotice(ANoticeVO noticeVO);
    // // 중복확인
    // boolean isAnswerOk(String inquiry_idx);
    // 답변 수정
    int updateNotice(ANoticeVO answerVO);
    // 팝업 삽입
    int insertPopup(APopupVO popupVO);
    // 팝업 삭제
    boolean deletePopup(String popup_idx);
    // 팝업 수정
    int updatePopupVisibility(APopupVO popupVO);
} 
