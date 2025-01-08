package com.ict.camping.domain.regularMeeting.vo;

import com.ict.camping.domain.users.vo.UsersVO;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class RegularMeetingBoardCommentVO {
    private int comment_idx;         // commentIdx -> comment_idx
    private String user_idx;         // userIdx -> user_idx (String 타입)
    private int board_idx;           // boardIdx -> board_idx
    private String comment_content;  // commentContent -> comment_content
    private Integer parent_id;       // parentId -> parent_id
    private Timestamp uploaded_at;   // uploadedAt -> uploaded_at

    private UsersVO user; // 작성자 정보
}
