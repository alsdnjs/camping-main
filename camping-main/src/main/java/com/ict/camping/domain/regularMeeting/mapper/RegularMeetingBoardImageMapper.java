package com.ict.camping.domain.regularMeeting.mapper;

import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardImageVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface RegularMeetingBoardImageMapper {

    @Insert("INSERT INTO regular_meeting_board_images (image_url) VALUES (#{image_url})")
    @Options(useGeneratedKeys = true, keyProperty = "image_idx")
    void insertImage(RegularMeetingBoardImageVO imageVO);
}
