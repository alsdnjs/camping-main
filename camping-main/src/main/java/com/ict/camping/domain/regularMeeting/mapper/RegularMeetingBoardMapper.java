package com.ict.camping.domain.regularMeeting.mapper;

import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardLikeVO;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardVO;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardCommentVO;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardImageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegularMeetingBoardMapper {

    // ========== 게시물 관련 ==========
    List<RegularMeetingBoardVO> selectAllBoards(@Param("meeting_idx") int meetingIdx);
    RegularMeetingBoardVO selectBoardById(@Param("board_idx") int boardIdx);
    void insertBoard(RegularMeetingBoardVO boardVO);
    void updateBoard(RegularMeetingBoardVO boardVO);
    void deleteBoard(@Param("board_idx") int boardIdx);

    // ========== 댓글 관련 ==========
    List<RegularMeetingBoardCommentVO> selectCommentsByBoardId(@Param("board_idx") int boardIdx);
    RegularMeetingBoardCommentVO selectCommentById(@Param("comment_idx") int commentIdx);
    void insertComment(RegularMeetingBoardCommentVO commentVO);
    void updateComment(RegularMeetingBoardCommentVO commentVO);
    void deleteComment(@Param("comment_idx") int commentIdx);

    // ========== 좋아요 관련 ==========
    // (기존 selectLike -> 단일 row)z
    RegularMeetingBoardLikeVO selectLike(@Param("board_idx") int board_idx, @Param("user_idx") String user_idx);

    void insertLike(RegularMeetingBoardLikeVO likeVO);
    void deleteLike(@Param("likes_idx") int likes_idx);

    // ========== 이미지 관련 ==========
    void insertImage(RegularMeetingBoardImageVO imageVO);
}
