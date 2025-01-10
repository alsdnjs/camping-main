package com.ict.camping.domain.chat.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.ict.camping.domain.chat.vo.ChatMessageVO;
import com.ict.camping.domain.chat.vo.ChatRoomVO;

public interface ChatService {
  // 채팅방 생성
    ChatRoomVO createChatRoom(int meeting_idx);

    // 특정 모임의 채팅방 조회
    ChatRoomVO getChatRoomByMeeting(int meeting_idx);

    // 메시지 전송(DB저장 + 알림)
    void sendMessage(ChatMessageVO msg);

    // 특정 채팅방의 메시지들 조회
    List<ChatMessageVO> getMessages(@PathVariable("roomId") int roomId);

    ChatRoomVO findChatRoomByRoomIdx(int roomId);

    // 사용자별 채팅방 목록 조회
    // public List<ChatRoomVO> getChatRoomsByUser(int userIdx);

    // String getAvatarUrlByUserIdx(int sender_idx);

}
