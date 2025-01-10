package com.ict.camping.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageVO {
    private int message_idx;
    private int room_idx;
    private int sender_idx;
    private String content;
    private String message_type; // text, image, file
    private String created_at;
    private String file_url;
    private String sender_avatar_url, sender_nickname;

    // 파일 전송 시 Base64 데이터, 파일명, 파일타입을 프론트에서 보낼 수 있다.
    private String fileName;
    private String fileType;
    private String fileData;
}
