package com.ict.camping.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVO {
    private int notification_id;
    private int user_idx;
    private String message, created_at;
    private boolean is_read;
    private String type; // 'general' or 'chat', etc
    

}