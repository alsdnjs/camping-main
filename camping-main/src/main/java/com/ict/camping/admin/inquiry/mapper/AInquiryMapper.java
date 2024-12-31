package com.ict.camping.admin.inquiry.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.admin.inquiry.vo.AInquiryAnswerVO;
import com.ict.camping.admin.inquiry.vo.AInquiryVO;
@Mapper
public interface AInquiryMapper {

    List<AInquiryVO> findAllInquiry();

    AInquiryVO findInquiryDetail(String inquiry_idx);

    AInquiryAnswerVO findInquiryAnswer(String inquiry_idx);

    int insertAnswer(AInquiryAnswerVO answerVO);

    int updateAnswer(AInquiryAnswerVO answerVO);

    int checkAnswer(String inquiry_idx);
}
