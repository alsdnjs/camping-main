package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardLikeVO;

public interface RegularMeetingBoardLikeService {
    RegularMeetingBoardLikeVO toggleLike(int board_idx, String user_idx); // user_idx를 String 타입으로 유지
}
