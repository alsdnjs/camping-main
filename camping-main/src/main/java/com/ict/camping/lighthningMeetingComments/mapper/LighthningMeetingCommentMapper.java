// package: com.ict.camping.lighthningMeetingComments.mapper

package com.ict.camping.lighthningMeetingComments.mapper;

import com.ict.camping.lighthningMeetingComments.vo.LighthningMeetingCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LighthningMeetingCommentMapper {

    List<LighthningMeetingCommentVO> getCommentsByPostId(int postId);

    void insertComment(LighthningMeetingCommentVO comment);

    void updateComment(LighthningMeetingCommentVO comment);

    void deleteComment(int commentId);

    LighthningMeetingCommentVO getCommentById(@Param("comment_idx") int comment_idx); // snake_case 사용
}
