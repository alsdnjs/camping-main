package com.ict.camping.admin.notices.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.ict.camping.admin.notices.vo.ANoticeVO;
import com.ict.camping.admin.notices.vo.APopupVO;
@Mapper
public interface ANoticeMapper {
    // 공지사항 리스트
    List<ANoticeVO> findAllNotice();
    // 팝업 리스트
    List<APopupVO> findAllPopups();
    // 공지사항 하나보기
    ANoticeVO findNoticeDetail(String notice_idx);
    // 공지사항 등록
    int insertNotice(ANoticeVO noticeVO);
    // 공지사항 삭제
    int deleteNotice(String notice_idx);
    // 공지사항 수정
    int updateNotice(ANoticeVO noticeVO);
    // 팝업 삽입
    int insertPopup(APopupVO popupVO);
    // 팝업 삭제
    int deletePopup(String popup_idx);
    // 팝업 업데이트
    int updatePopupVisibility(APopupVO popupVO);

}
