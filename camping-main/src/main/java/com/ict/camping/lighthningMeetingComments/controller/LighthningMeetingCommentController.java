// package: com.ict.camping.lighthningMeetingComments.controller

package com.ict.camping.lighthningMeetingComments.controller;

import com.ict.camping.lighthningMeetingComments.service.LighthningMeetingCommentService;
import com.ict.camping.lighthningMeetingComments.vo.LighthningMeetingCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meetings")
public class LighthningMeetingCommentController {

    @Autowired
    private LighthningMeetingCommentService commentService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Map<String, Object>>> getComments(@PathVariable("id") int postId) {
        System.out.println("Received ID in Controller: " + postId);
        List<Map<String, Object>> hierarchicalComments = commentService.getHierarchicalCommentsByPostId(postId);
        return ResponseEntity.ok(hierarchicalComments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable("id") int postId, @RequestBody LighthningMeetingCommentVO comment) {
        System.out.println("Parent ID in Controller: " + comment.getParent_id()); // parentId 출력
        System.out.println("Post ID: " + postId);

        try {
            comment.setPost_idx(postId);

            if (comment.getParent_id() != null) {
                LighthningMeetingCommentVO parentComment = commentService.getCommentById(comment.getParent_id());
                if (parentComment == null) {
                    throw new IllegalArgumentException("Invalid parent_id: 존재하지 않는 댓글입니다.");
                }
            }

            // 댓글 추가 후 저장된 댓글 정보 반환
            LighthningMeetingCommentVO savedComment = commentService.addComment(comment);
            return ResponseEntity.ok(Map.of(
                "message", "댓글 작성 성공",
                "comment_idx", savedComment.getComment_idx()
            ));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "알 수 없는 오류"));
        }
    }

    // 댓글 수정 엔드포인트
    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") int postId,
            @PathVariable("commentId") int commentId,
            @RequestBody Map<String, Object> payload
    ) {
        String newContent = (String) payload.get("content");
        if (newContent == null || newContent.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "내용이 비어 있습니다."));
        }

        try {
            LighthningMeetingCommentVO comment = commentService.getCommentById(commentId);
            if (comment == null) {
                return ResponseEntity.status(404).body(Map.of("error", "댓글을 찾을 수 없습니다."));
            }

            comment.setContent(newContent);
            commentService.updateComment(comment);
            return ResponseEntity.ok(Map.of("message", "댓글 수정 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "알 수 없는 오류"));
        }
    }

    // 댓글 삭제 엔드포인트
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("id") int postId,
            @PathVariable("commentId") int commentId
    ) {
        try {
            LighthningMeetingCommentVO comment = commentService.getCommentById(commentId);
            if (comment == null) {
                return ResponseEntity.status(404).body(Map.of("error", "댓글을 찾을 수 없습니다."));
            }

            commentService.deleteComment(commentId);
            return ResponseEntity.ok(Map.of("message", "댓글 삭제 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "알 수 없는 오류"));
        }
    }
}
