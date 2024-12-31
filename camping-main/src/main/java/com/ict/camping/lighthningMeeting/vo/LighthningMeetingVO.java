package com.ict.camping.lighthningMeeting.vo;

import lombok.Data;

@Data
public class LighthningMeetingVO {
    private int post_idx;
    private String title;
    private String content;
    private int writer_idx;
    private String writer_id; // writer_id 추가
    private String meeting_date;   // snake_case
    private String meeting_location;
    private String created_at;
    private int personnel;
}
