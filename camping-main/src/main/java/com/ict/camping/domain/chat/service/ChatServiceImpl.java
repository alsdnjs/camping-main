package com.ict.camping.domain.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ict.camping.domain.chat.mapper.ChatMapper;
import com.ict.camping.domain.chat.vo.ChatMessageVO;
import com.ict.camping.domain.chat.vo.ChatRoomVO;
import com.ict.camping.domain.chat.vo.NotificationVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

  private final ChatMapper chatMapper;

  @Override
  public ChatRoomVO createChatRoom(int meeting_idx) {
    // 채팅방 vo생성
    ChatRoomVO vo = new ChatRoomVO();
    vo.setMeeting_idx(meeting_idx);

    // db
    chatMapper.createChatRoom(vo);

    // 생성된 채팅방 가져오기 (room_idx 조회)
    ChatRoomVO createdRoom = chatMapper.findChatRoomByMeeting(meeting_idx);
    return createdRoom;
  }

  @Override
  public ChatRoomVO getChatRoomByMeeting(int meeting_idx) {
    return chatMapper.findChatRoomByMeeting(meeting_idx);
  }

  @Override
  public void sendMessage(ChatMessageVO msg) {
      // 1) 파일 전송 처리 (필요 시 구현)
      if ("file".equals(msg.getMessage_type()) && msg.getFileData() != null) {
          // 파일 저장 로직 (예: 서버나 S3에 업로드 후 file_url 설정)
          // 예시:
          // String fileUrl = fileStorageService.storeFile(msg.getFileData(), msg.getFileName());
          // msg.setFile_url(fileUrl);
      }
  
      // 2) DB에 메시지 저장
      chatMapper.insertMessage(msg);
  
      // 3) 알림 생성
      ChatRoomVO room = chatMapper.findChatRoomByRoomIdx(msg.getRoom_idx());
      if (room != null) {
          List<Integer> userIdxList = chatMapper.findAllUserIdxByMeeting(room.getMeeting_idx());
          for (Integer uid : userIdxList) {
              if (uid == msg.getSender_idx()) {
                  continue; // 본인에게는 알림 생략
              }
              NotificationVO noti = new NotificationVO();
              noti.setUser_idx(uid);
              noti.setMessage("새 채팅 메시지가 도착했습니다.");
              noti.set_read(false);
              noti.setType("chat");
              chatMapper.insertNotification(noti);
          }
      }
  }
  

    @Override
    public List<ChatMessageVO> getMessages(int roomId) {
        return chatMapper.getMessagesByRoomIdx(roomId);
    }

    @Override
    public ChatRoomVO findChatRoomByRoomIdx(int roomId) {
        return chatMapper.findChatRoomByRoomIdx(roomId);
    }



    // @Override
    // public List<ChatRoomVO> getChatRoomsByUser(int userIdx) {
    //     return chatMapper.getChatRoomsByUser(userIdx);
    // }

    // @Override
    // public String getAvatarUrlByUserIdx(int sender_idx) {
    //     return chatMapper.findAvatarUrlByUserIdx(sender_idx);
    // }

    

}
