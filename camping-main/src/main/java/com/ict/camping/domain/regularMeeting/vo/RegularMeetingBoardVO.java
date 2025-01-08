package com.ict.camping.domain.regularMeeting.vo;

import com.ict.camping.domain.users.vo.UsersVO;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class RegularMeetingBoardVO {
    private int board_idx;             // boardIdx -> board_idx
    private Integer meeting_idx;       // meetingIdx -> meeting_idx
    private String user_idx;           // userIdx -> user_idx (String 타입)
    private String board_content;      // boardContent -> board_content
    private Integer image_idx;         // imageIdx -> image_idx
    private Timestamp uploaded_at;     // uploadedAt -> uploaded_at

    // 관계 설정
    private UsersVO user; // 작성자 정보
    private RegularMeetingBoardImageVO image; // 이미지 정보

    private List<RegularMeetingBoardCommentVO> comments; // 댓글 목록
    private List<RegularMeetingBoardLikeVO> likes; // 좋아요 목록 (필요 시 활성화)

    // ★ 추가: 쉼표 split한 여러 이미지 경로를 담을 필드
    private List<String> images;  


       // 추가: 기존 이미지 URLs
       private List<String> existingImages;
}
