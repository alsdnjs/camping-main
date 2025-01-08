package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.mapper.RegularMeetingBoardMapper;
import com.ict.camping.domain.regularMeeting.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class RegularMeetingBoardServiceImpl implements RegularMeetingBoardService {

    @Autowired
    private RegularMeetingBoardMapper boardMapper;

    @Autowired
    private RegularMeetingBoardImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(RegularMeetingBoardServiceImpl.class);

    @Override
    public List<RegularMeetingBoardVO> getAllBoards(int meeting_idx) {
        return boardMapper.selectAllBoards(meeting_idx);
    }

    @Override
    public RegularMeetingBoardVO createBoard(RegularMeetingBoardVO boardVO, List<MultipartFile> files) throws Exception {
        try {
            // 1) 이미지 업로드 처리
            String joinedImagePaths = null;
            if (files != null && !files.isEmpty()) {
                // 업로드: 여러 이미지가 있을 수 있음
                List<String> imageUrls = imageService.uploadImages(files);
                // 쉼표 구분자로 합침
                joinedImagePaths = String.join(",", imageUrls);
                logger.info("업로드된 이미지 경로(합친 문자열): {}", joinedImagePaths);
            }

            // 2) regular_meeting_board_images 테이블에 저장
            RegularMeetingBoardImageVO imageVO = new RegularMeetingBoardImageVO();
            if (joinedImagePaths != null) {
                imageVO.setImage_url(joinedImagePaths);
                imageService.saveImage(imageVO);
                logger.info("Saved imageVO: imageIdx={}, imageUrl={}", imageVO.getImage_idx(), imageVO.getImage_url());
                boardVO.setImage_idx(imageVO.getImage_idx());
            }

            // 3) 게시물 insert
            boardMapper.insertBoard(boardVO);
            logger.info("게시물 생성 완료: boardIdx={}", boardVO.getBoard_idx());

            // 4) 방금 insert한 게시물 다시 selectBoardById로 가져오기
            return getBoardById(boardVO.getBoard_idx());

        } catch (Exception e) {
            logger.error("게시물 생성 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public RegularMeetingBoardVO getBoardById(int board_idx) {
        // board + image_url(쉼표로 합쳐진) + comments + likes
        RegularMeetingBoardVO board = boardMapper.selectBoardById(board_idx);
        if (board != null) {
            // (1) 댓글 목록
            List<RegularMeetingBoardCommentVO> comments = boardMapper.selectCommentsByBoardId(board_idx);
            board.setComments(comments);

            // (2) image_url 쉼표 split → board.setImages(...)
            // 만약 board.getImage() == null 이거나, getImage().getImage_url() == null 이면 예외처리
            if (board.getImage() != null && board.getImage().getImage_url() != null) {
                String joinedPaths = board.getImage().getImage_url();  // "/upload/a.jpg,/upload/b.jpg"
                String[] splitted = joinedPaths.split(",");
                board.setImages(List.of(splitted));
            } else {
                // 이미지가 없는 경우 빈 리스트
                board.setImages(List.of());
            }
        }
        return board;
    }

    @Override
    public void updateBoard(RegularMeetingBoardVO boardVO, List<MultipartFile> files) throws Exception {
        try {
            // 1) 기존 이미지 처리
            // image_idx가 null인지 먼저 확인
            if (boardVO.getImage_idx() != null && boardVO.getImage_idx() != 0) { // 수정된 부분
                // 기존 이미지 삭제 또는 업데이트 로직 필요 시 추가
                // 예: 기존 이미지를 삭제하고 새로운 이미지로 대체
                // 현재는 로직이 없으므로 주석으로 남김
                // imageService.deleteImage(boardVO.getImage_idx());
            }

            // 2) 새로운 이미지 업로드 처리
            String joinedImagePaths = null;
            if (files != null && !files.isEmpty()) {
                List<String> imageUrls = imageService.uploadImages(files);
                joinedImagePaths = String.join(",", imageUrls);
                logger.info("업로드된 이미지 경로(합친 문자열): {}", joinedImagePaths);

                // 3) regular_meeting_board_images 테이블에 저장
                RegularMeetingBoardImageVO imageVO = new RegularMeetingBoardImageVO();
                imageVO.setImage_url(joinedImagePaths);
                imageService.saveImage(imageVO);
                logger.info("Saved imageVO: imageIdx={}, imageUrl={}", imageVO.getImage_idx(), imageVO.getImage_url());
                boardVO.setImage_idx(imageVO.getImage_idx());
            } else {
                // 이미지가 없는 경우 image_idx를 null로 설정하거나 기존 값을 유지
                // 여기서는 image_idx를 null로 설정
                boardVO.setImage_idx(null); // 수정된 부분
            }

            // 4) 게시물 업데이트
            boardMapper.updateBoard(boardVO);
            logger.info("게시물 수정 완료: boardIdx={}", boardVO.getBoard_idx());

        } catch (Exception e) {
            logger.error("게시물 수정 실패: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteBoard(int board_idx) {
        boardMapper.deleteBoard(board_idx);
    }

    @Override
    public List<RegularMeetingBoardCommentVO> getCommentsByBoardId(int board_idx) {
        return boardMapper.selectCommentsByBoardId(board_idx);
    }

    @Override
    public RegularMeetingBoardCommentVO getCommentById(int comment_idx) {
        return boardMapper.selectCommentById(comment_idx);
    }

    @Override
    public RegularMeetingBoardCommentVO addComment(RegularMeetingBoardCommentVO commentVO) {
        // 부모 댓글 존재 여부 확인
        if (commentVO.getParent_id() != null && commentVO.getParent_id() != 0) {
            RegularMeetingBoardCommentVO parentComment = boardMapper.selectCommentById(commentVO.getParent_id());
            if (parentComment == null) {
                throw new IllegalArgumentException("Invalid parent_id: 존재하지 않는 댓글입니다.");
            }
        }

        boardMapper.insertComment(commentVO);
        return boardMapper.selectCommentById(commentVO.getComment_idx());
    }

    @Override
    public void updateComment(RegularMeetingBoardCommentVO commentVO) {
        boardMapper.updateComment(commentVO);
    }

    @Override
    public void deleteComment(int comment_idx) {
        boardMapper.deleteComment(comment_idx);
    }
}
