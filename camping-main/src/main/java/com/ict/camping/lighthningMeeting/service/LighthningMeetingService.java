package com.ict.camping.lighthningMeeting.service;

import com.ict.camping.lighthningMeeting.mapper.LighthningMeetingMapper;
import com.ict.camping.lighthningMeeting.vo.LighthningMeetingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LighthningMeetingService {

    @Autowired
    private LighthningMeetingMapper mapper;

    

    public List<LighthningMeetingVO> getAllMeetings() {
        return mapper.selectAllMeetings();
    }

    public LighthningMeetingVO getMeetingById(int id) {
        return mapper.selectMeetingById(id);
    }

    public void createMeeting(LighthningMeetingVO meetingVO) {
        mapper.insertMeeting(meetingVO);
    }

    public void updateMeeting(LighthningMeetingVO meetingVO) {
        mapper.updateMeeting(meetingVO);
    }

    public void deleteMeeting(int id) {
        mapper.deleteMeeting(id);
    }
}
