package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.vo.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface RegularMeetingBoardService {
    List<RegularMeetingBoardVO> getAllBoards(int meeting_idx);

    RegularMeetingBoardVO getBoardById(int board_idx);

    RegularMeetingBoardVO createBoard(RegularMeetingBoardVO boardVO, List<MultipartFile> files) throws Exception;

    void updateBoard(RegularMeetingBoardVO boardVO, List<MultipartFile> files) throws Exception;

    void deleteBoard(int board_idx);

    List<RegularMeetingBoardCommentVO> getCommentsByBoardId(int board_idx);

    RegularMeetingBoardCommentVO getCommentById(int comment_idx);

    RegularMeetingBoardCommentVO addComment(RegularMeetingBoardCommentVO commentVO);

    void updateComment(RegularMeetingBoardCommentVO commentVO);

    void deleteComment(int comment_idx);
}
