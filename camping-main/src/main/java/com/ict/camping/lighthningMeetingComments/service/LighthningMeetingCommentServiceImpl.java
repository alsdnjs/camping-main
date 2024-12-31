// package: com.ict.camping.lighthningMeetingComments.service

package com.ict.camping.lighthningMeetingComments.service;

import com.ict.camping.lighthningMeetingComments.mapper.LighthningMeetingCommentMapper;
import com.ict.camping.lighthningMeetingComments.vo.LighthningMeetingCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LighthningMeetingCommentServiceImpl implements LighthningMeetingCommentService {

    @Autowired
    private LighthningMeetingCommentMapper commentMapper;

    @Override
    public List<Map<String, Object>> getHierarchicalCommentsByPostId(int postId) {
        List<LighthningMeetingCommentVO> comments = commentMapper.getCommentsByPostId(postId);

        Map<Integer, Map<String, Object>> commentMap = new HashMap<>();
        List<Map<String, Object>> topLevelComments = new ArrayList<>();

        // 모든 댓글을 맵에 추가
        for (LighthningMeetingCommentVO comment : comments) {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("comment_idx", comment.getComment_idx());
            commentData.put("post_idx", comment.getPost_idx());
            commentData.put("writer_idx", comment.getWriter_idx());
            commentData.put("writer_id", comment.getWriter_id());
            commentData.put("content", comment.getContent());
            commentData.put(
                "created_at",
                comment.getCreated_at() != null ? comment.getCreated_at().toInstant().toString() : null
            );
            commentData.put("children", new ArrayList<Map<String, Object>>());

            commentMap.put(comment.getComment_idx(), commentData);

            if (comment.getParent_id() == null) {
                topLevelComments.add(commentData);
            }
        }

        // 모든 댓글을 순회하며 자식 댓글을 추가
        for (LighthningMeetingCommentVO comment : comments) {
            if (comment.getParent_id() != null) {
                Map<String, Object> parentComment = commentMap.get(comment.getParent_id());
                if (parentComment != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parentComment.get("children");
                    children.add(commentMap.get(comment.getComment_idx()));
                } else {
                    System.err.println("Parent comment not found for comment ID: " + comment.getComment_idx());
                }
            }
        }

        return topLevelComments;
    }

    @Override
    public LighthningMeetingCommentVO addComment(LighthningMeetingCommentVO comment) {
        if (comment.getParent_id() != null && comment.getParent_id() != 0) {
            LighthningMeetingCommentVO parentComment = commentMapper.getCommentById(comment.getParent_id());
            if (parentComment == null) {
                throw new IllegalArgumentException("Invalid parent_id: 존재하지 않는 댓글입니다.");
            }
        }

        commentMapper.insertComment(comment);

        // 로그를 통해 comment_idx 확인
        System.out.println("Inserted Comment IDX: " + comment.getComment_idx());

        // Return the saved comment using the generated comment_idx
        return commentMapper.getCommentById(comment.getComment_idx());
    }

    @Override
    public void updateComment(LighthningMeetingCommentVO comment) {
        commentMapper.updateComment(comment);
    }

    @Override
    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }

    @Override
    public boolean isParentCommentExists(int parentId) {
        return commentMapper.getCommentById(parentId) != null;
    }

    @Override
    public LighthningMeetingCommentVO getCommentById(int commentId) {
        return commentMapper.getCommentById(commentId);
    }
}
