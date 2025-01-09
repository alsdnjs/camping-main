package com.ict.camping.domain.regularMeeting.controller;

import com.ict.camping.domain.regularMeeting.service.RegularMeetingBoardLikeService;
import com.ict.camping.domain.regularMeeting.service.RegularMeetingBoardService;
import com.ict.camping.domain.regularMeeting.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/regular-meeting-board")
public class RegularMeetingBoardController {

    @Autowired
    private RegularMeetingBoardService boardService;



    @Autowired
    private RegularMeetingBoardLikeService likeService; // LikeService 주입

    private static final Logger logger = LoggerFactory.getLogger(RegularMeetingBoardController.class);

    // 게시물 전체 조회
    @GetMapping("/meetings/{meetingIdx}/boards")
    public ResponseEntity<List<RegularMeetingBoardVO>> getAllBoards(@PathVariable("meetingIdx") int meetingIdx) {
        List<RegularMeetingBoardVO> boards = boardService.getAllBoards(meetingIdx);
        return ResponseEntity.ok(boards);
    }

    // 특정 게시물 조회
    @GetMapping("/boards/{boardIdx}")
    public ResponseEntity<RegularMeetingBoardVO> getBoardById(@PathVariable("boardIdx") int boardIdx) {
        RegularMeetingBoardVO board = boardService.getBoardById(boardIdx);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(board);
    }

    // 게시물 생성
    @PostMapping("/boards")
    public ResponseEntity<RegularMeetingBoardVO> createBoard(
            @RequestParam("meeting_idx") int meeting_idx,
            @RequestParam("user_idx") String user_idx, // int에서 String으로 변경
            @RequestParam("board_content") String board_content,
            @RequestParam(name = "files", required = false) List<MultipartFile> files // 매개변수 이름 명시
    ) {
        try {
            RegularMeetingBoardVO boardVO = new RegularMeetingBoardVO();
            boardVO.setMeeting_idx(meeting_idx);
            boardVO.setUser_idx(user_idx);
            boardVO.setBoard_content(board_content);

            RegularMeetingBoardVO createdBoard = boardService.createBoard(boardVO, files);
            return ResponseEntity.status(201).body(createdBoard);
        } catch (Exception e) {
            logger.error("게시물 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    // 게시물 수정
    @PutMapping("/boards/{boardIdx}")
    public ResponseEntity<Void> updateBoard(
            @PathVariable("boardIdx") int boardIdx,
            @RequestParam("board_content") String board_content,
            @RequestParam(name = "files", required = false) List<MultipartFile> files, // 매개변수 이름 명시,
            @RequestParam(name =" files", required = false) List<String> existing_images // 기존 이미지 URLs 받기
    ) {
        try {
            RegularMeetingBoardVO boardVO = new RegularMeetingBoardVO();
            boardVO.setBoard_idx(boardIdx);
            boardVO.setBoard_content(board_content);
            boardVO.setExistingImages(existing_images); // 기존 이미지 설정

            boardService.updateBoard(boardVO, files);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("게시물 수정 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    // 게시물 삭제
    @DeleteMapping("/boards/{boardIdx}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("boardIdx") int boardIdx) {
        try {
            boardService.deleteBoard(boardIdx);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("게시물 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    // 댓글 추가
    @PostMapping("/comments")
    public ResponseEntity<RegularMeetingBoardCommentVO> addComment(@RequestBody RegularMeetingBoardCommentVO commentVO) {
        try {
            RegularMeetingBoardCommentVO createdComment = boardService.addComment(commentVO);
            return ResponseEntity.status(201).body(createdComment);
        } catch (IllegalArgumentException e) {
            logger.warn("댓글 추가 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("댓글 추가 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    // 댓글 수정
    @PutMapping("/comments/{commentIdx}")
    public ResponseEntity<?> updateComment(
            @PathVariable("commentIdx") int commentIdx,
            @RequestBody RegularMeetingBoardCommentVO commentVO
    ) {
        try {
            // 댓글 인덱스를 VO에 설정
            commentVO.setComment_idx(commentIdx);

            // 댓글 수정 서비스 호출
            boardService.updateComment(commentVO);

            return ResponseEntity.ok().body("댓글이 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            logger.warn("댓글 수정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("댓글 수정 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("댓글 수정에 실패했습니다.");
        }
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentIdx}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentIdx") int commentIdx) {
        try {
            boardService.deleteComment(commentIdx);
            return ResponseEntity.ok().body("댓글이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            logger.warn("댓글 삭제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("댓글 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("댓글 삭제에 실패했습니다.");
        }
    }

    // 좋아요 토글
    @PostMapping("/boards/{boardIdx}/likes")
    public ResponseEntity<RegularMeetingBoardLikeVO> toggleLike(
            @PathVariable("boardIdx") int boardIdx,
            @RequestParam("user_idx") String user_idx
    ) {
        try {
            RegularMeetingBoardLikeVO likeVO = likeService.toggleLike(boardIdx, user_idx);
            if (likeVO == null) {
                // 좋아요 취소됨
                logger.info("좋아요 취소됨: boardIdx={}, user_idx={}", boardIdx, user_idx);
                return ResponseEntity.ok().build(); 
            } else {
                // 좋아요 추가됨
                logger.info("좋아요 추가됨: boardIdx={}, user_idx={}", boardIdx, user_idx);
                return ResponseEntity.ok(likeVO);
            }
        } catch (Exception e) {
            logger.error("좋아요 토글 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    // 기타 필요한 엔드포인트들...


    
}
