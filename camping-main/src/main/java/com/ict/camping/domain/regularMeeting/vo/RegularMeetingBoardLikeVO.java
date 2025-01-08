package com.ict.camping.domain.regularMeeting.vo;

import lombok.Data;

@Data
public class RegularMeetingBoardLikeVO {
    private int likes_idx;    // likesIdx -> likes_idx
    private String user_idx;  // userIdx -> user_idx (String 타입)
    private int board_idx;    // boardIdx -> board_idx
}
