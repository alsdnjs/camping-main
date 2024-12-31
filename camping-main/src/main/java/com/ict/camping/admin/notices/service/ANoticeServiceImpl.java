package com.ict.camping.admin.notices.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.admin.notices.mapper.ANoticeMapper;
import com.ict.camping.admin.notices.vo.ANoticeVO;


@Service
public class ANoticeServiceImpl implements ANoticeService{

    @Autowired
    private ANoticeMapper noticeMapper;

    @Override
    public List<ANoticeVO> getAllNotices() {
        return noticeMapper.findAllNotice();
    }

    @Override
    public int insertNotice(ANoticeVO noticeVO) {
        return noticeMapper.insertNotice(noticeVO);
    }

    @Override
    public ANoticeVO getNoticeDetail(String notice_idx) {
        return noticeMapper.findNoticeDetail(notice_idx);
    }



    
}
