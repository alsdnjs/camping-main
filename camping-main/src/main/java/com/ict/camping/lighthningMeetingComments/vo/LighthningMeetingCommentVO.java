package com.ict.camping.lighthningMeetingComments.vo;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class LighthningMeetingCommentVO {
    private int comment_idx;
    private int post_idx;
    private int writer_idx;
    private String writer_id; // 작성자 ID 추가
    private String content;
    private Timestamp created_at; // snake_case
    private Integer parent_id;     // 대댓글일 경우 부모 댓글 ID
    private List<LighthningMeetingCommentVO> children; // 자식 댓글 리스트
}


