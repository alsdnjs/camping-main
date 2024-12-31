package com.ict.camping.admin.inquiry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.admin.admin.mapper.AdminMapper;
import com.ict.camping.admin.inquiry.mapper.AInquiryMapper;
import com.ict.camping.admin.inquiry.vo.AInquiryAnswerVO;
import com.ict.camping.admin.inquiry.vo.AInquiryVO;


@Service
public class AInquiryServiceImpl implements AInquiryService{
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AInquiryMapper inquiryMapper;

    @Override
    public List<AInquiryVO> getAllInquiry() {
        return inquiryMapper.findAllInquiry();
    }

    @Override
    public AInquiryVO getInquiryDetail(String inquiry_idx) {
        return inquiryMapper.findInquiryDetail(inquiry_idx);
    }

    @Override
    public boolean deleteAdmin(String admin_idx) {
        int rowsAffected = adminMapper.deleteAdmin(admin_idx);
        return rowsAffected > 0;
    }

    @Override
    public boolean isAnswerOk(String inquiry_idx) {
        return inquiryMapper.checkAnswer(inquiry_idx) > 0;
    }

    @Override
    public int insertAnswer(AInquiryAnswerVO answerVO) {
        return inquiryMapper.insertAnswer(answerVO);
    }

    @Override
    public AInquiryAnswerVO getInquiryAnswer(String inquiry_idx) {
        return inquiryMapper.findInquiryAnswer(inquiry_idx);
    }

    @Override
    public int updateAnswer(AInquiryAnswerVO answerVO) {
        return inquiryMapper.updateAnswer(answerVO);
    }

    
}
