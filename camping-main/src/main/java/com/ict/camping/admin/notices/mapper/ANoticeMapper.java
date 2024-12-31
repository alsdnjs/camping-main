package com.ict.camping.admin.notices.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.admin.notices.vo.ANoticeVO;
@Mapper
public interface ANoticeMapper {

    List<ANoticeVO> findAllNotice();

    ANoticeVO findNoticeDetail(String notice_idx);

    // AInquiryAnswerVO findInquiryAnswer(String inquiry_idx);

    int insertNotice(ANoticeVO noticeVO);

    // int updateAnswer(AInquiryAnswerVO answerVO);

    // int checkAnswer(String inquiry_idx);
}
