package com.ict.camping.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomVO {
    private int room_idx, meeting_idx;
    private String meeting_name;
    private String avatar_url;
    private String profile_image;
}
