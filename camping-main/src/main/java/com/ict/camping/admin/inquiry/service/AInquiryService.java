package com.ict.camping.admin.inquiry.service;

import java.util.List;

import com.ict.camping.admin.inquiry.vo.AInquiryAnswerVO;
import com.ict.camping.admin.inquiry.vo.AInquiryVO;

public interface AInquiryService {
    // 리스트
    List<AInquiryVO> getAllInquiry();
    // 디테일
    AInquiryVO getInquiryDetail(String inquiry_idx);
    // answer 디테일
    AInquiryAnswerVO getInquiryAnswer(String inquiry_idx);
    // 삭제
    boolean deleteAdmin(String admin_idx);
    // 삽입
    int insertAnswer(AInquiryAnswerVO answerVO);
    // 중복확인
    boolean isAnswerOk(String inquiry_idx);
    // 답변 수정
    int updateAnswer(AInquiryAnswerVO answerVO);
} 
