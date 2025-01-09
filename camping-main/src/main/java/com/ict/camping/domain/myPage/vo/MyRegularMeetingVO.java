package com.ict.camping.domain.myPage.vo;

import lombok.Data;

@Data
public class MyRegularMeetingVO {
    private String meeting_idx, name, description, profile_image, region, subregion, personnel, leader_idx, created_at;
    private String numberOfPeople;
}