package com.ict.camping.admin.notices.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.admin.notices.mapper.ANoticeMapper;
import com.ict.camping.admin.notices.vo.ANoticeVO;
import com.ict.camping.admin.notices.vo.APopupVO;


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

    @Override
    public boolean deleteNotice(String notice_idx) {
        int rowsAffected = noticeMapper.deleteNotice(notice_idx);
        return rowsAffected > 0;
    }

    @Override
    public int updateNotice(ANoticeVO answerVO) {
        return noticeMapper.updateNotice(answerVO);
    }

    @Override
    public List<APopupVO> getAllPopups() {
        return noticeMapper.findAllPopups();
    }

    @Override
    public int insertPopup(APopupVO popupVO) {
        return noticeMapper.insertPopup(popupVO);
    }

    @Override
    public boolean deletePopup(String popup_idx) {
       int rowsAffected = noticeMapper.deletePopup(popup_idx);
       return rowsAffected > 0;
    }

    @Override
    public int updatePopupVisibility(APopupVO popupVO) {
        return noticeMapper.updatePopupVisibility(popupVO);
    }

    
}
