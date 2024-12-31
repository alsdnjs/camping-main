package com.ict.camping.lighthningMeeting.mapper;

import com.ict.camping.lighthningMeeting.vo.LighthningMeetingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LighthningMeetingMapper {
    List<LighthningMeetingVO> selectAllMeetings();
    LighthningMeetingVO selectMeetingById(int id);
    void insertMeeting(LighthningMeetingVO meetingVO);
    void updateMeeting(LighthningMeetingVO meetingVO);
    void deleteMeeting(int id);
}
