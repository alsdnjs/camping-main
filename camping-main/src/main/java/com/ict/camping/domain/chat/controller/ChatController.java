package com.ict.camping.domain.chat.controller;

import com.ict.camping.domain.chat.vo.ChatMessageVO;
import com.ict.camping.domain.chat.vo.ChatRoomVO;

import lombok.RequiredArgsConstructor;

import com.ict.camping.domain.chat.service.ChatService;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @Autowired
    public void ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 채팅방 조회
    @GetMapping("/room/{meeting_idx}")
    public ResponseEntity<ChatRoomVO> getChatRoom(@PathVariable("meeting_idx") int meeting_idx) {
        ChatRoomVO room = chatService.getChatRoomByMeeting(meeting_idx);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<ChatRoomVO> createChatRoom(@RequestBody ChatRoomVO chatRoom) {
        ChatRoomVO createdRoom = chatService.createChatRoom(chatRoom.getMeeting_idx());
        return ResponseEntity.ok(createdRoom);
    }

    // 사용자별 채팅방 목록 조회
    // @GetMapping("/rooms")
    // public ResponseEntity<List<ChatRoomVO>> getChatRooms(@Param("user_idx") int
    // userIdx) {
    // List<ChatRoomVO> chatRooms = chatService.getChatRoomsByUser(userIdx);
    // return ResponseEntity.ok(chatRooms);
    // }

    // 채팅 메시지 기록 조회
    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<ChatMessageVO>> getMessages(@PathVariable("roomId") int roomId) {
        List<ChatMessageVO> messages = chatService.getMessages(roomId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/room-info/{roomId}")
    public ResponseEntity<ChatRoomVO> getChatRoomInfo(@PathVariable("roomId") int roomId) {
        ChatRoomVO room = chatService.findChatRoomByRoomIdx(roomId);
        if (room != null)
            return ResponseEntity.ok(room);
        else
            return ResponseEntity.notFound().build();
    }

}
