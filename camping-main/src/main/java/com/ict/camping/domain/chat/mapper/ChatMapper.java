package com.ict.camping.domain.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ict.camping.domain.chat.vo.ChatMessageVO;
import com.ict.camping.domain.chat.vo.ChatRoomVO;
import com.ict.camping.domain.chat.vo.NotificationVO;

@Mapper
public interface ChatMapper {
 // 채팅방 생성
 void createChatRoom(ChatRoomVO vo);

 // 특정 모임의 채팅방 조회
 ChatRoomVO findChatRoomByMeeting(@Param("meeting_idx") int meeting_idx);
 
 // 특정 room_idx로 채팅방 조회 추가
 ChatRoomVO findChatRoomByRoomIdx(@Param("roomId") int roomId);
 
 // 메시지 삽입
 void insertMessage(ChatMessageVO msg);

 // 특정 채팅방의 메시지들 조회
 List<ChatMessageVO> getMessagesByRoomIdx(@Param("roomId") int roomId);

 // 알림 삽입
 void insertNotification(NotificationVO noti);

 // 모임에 속한 모든 user_idx 조회
 List<Integer> findAllUserIdxByMeeting(@Param("meeting_idx") int meetingIdx);

 // 사용자별 채팅방 목록 조회
 // public List<ChatRoomVO> getChatRoomsByUser(@Param("user_idx") int userIdx);

String findAvatarUrlByUserIdx(int sender_idx);
}
