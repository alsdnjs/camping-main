// package: com.ict.camping.lighthningMeetingComments.service

package com.ict.camping.lighthningMeetingComments.service;

import com.ict.camping.lighthningMeetingComments.vo.LighthningMeetingCommentVO;

import java.util.List;
import java.util.Map;

public interface LighthningMeetingCommentService {

    List<Map<String, Object>> getHierarchicalCommentsByPostId(int postId);

    LighthningMeetingCommentVO addComment(LighthningMeetingCommentVO comment);

    void updateComment(LighthningMeetingCommentVO comment);

    void deleteComment(int commentId);

    boolean isParentCommentExists(int parentId);

    LighthningMeetingCommentVO getCommentById(int commentId);
}
