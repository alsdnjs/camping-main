package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.mapper.RegularMeetingBoardMapper;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardLikeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegularMeetingBoardLikeServiceImpl implements RegularMeetingBoardLikeService {

    @Autowired
    private RegularMeetingBoardMapper boardMapper;

    @Override
    @Transactional
    public RegularMeetingBoardLikeVO toggleLike(int board_idx, String user_idx) {
        // 1) 기존 좋아요가 있는지 확인
        RegularMeetingBoardLikeVO existingLike = boardMapper.selectLike(board_idx, user_idx);

        if (existingLike != null) {
            // 2) 이미 좋아요가 있다면 -> 좋아요 취소 (delete)
            boardMapper.deleteLike(existingLike.getLikes_idx());
            return null; // 좋아요 취소됨
        } else {
            // 3) 좋아요가 없다면 -> 새로 insert
            RegularMeetingBoardLikeVO newLike = new RegularMeetingBoardLikeVO();
            newLike.setBoard_idx(board_idx);
            newLike.setUser_idx(user_idx);
            boardMapper.insertLike(newLike);
            return newLike; // 좋아요 추가됨
        }
    }
}
