package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.mapper.RegularMeetingMapper;
import com.ict.camping.domain.regularMeeting.vo.HashtagVO;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegularMeetingServiceImpl implements RegularMeetingService {

    @Autowired
    private RegularMeetingMapper regularMeetingMapper;

    // RegularMeetingServiceImpl.java

@Override
@Transactional
public int createMeeting(RegularMeetingVO meeting, List<String> hashtags) {
    // 1) 모임 추가
    regularMeetingMapper.insertMeeting(meeting);
    int meetingIdx = meeting.getMeeting_idx();

    // 2) 해시태그 처리
    for (String tagName : hashtags) {
        if (tagName == null || tagName.trim().isEmpty()) continue;
        String trimTag = tagName.trim();

        // 해시태그 존재 여부 확인
        int hashtagIdx = findHashtagByName(trimTag);
        if (hashtagIdx > 0) {
            // 해시태그가 이미 존재하면 연결
            regularMeetingMapper.insertMeetingHashtags(meetingIdx, hashtagIdx);
        } else {
            // 해시태그가 존재하지 않으면 생성 후 연결
            HashtagVO newHashtag = new HashtagVO();
            newHashtag.setName(trimTag);
            regularMeetingMapper.insertMeetingHashtags(meeting.getMeeting_idx(), hashtagIdx);
            hashtagIdx = newHashtag.getHashtag_idx();
            if (hashtagIdx > 0) {
                regularMeetingMapper.insertMeetingHashtags(meetingIdx, hashtagIdx);
            }
        }
    }

    return meetingIdx;
}

    @Override
    public List<RegularMeetingVO> getAllMeetings(int userIdx) {
        return regularMeetingMapper.selectAllMeetings(userIdx);
    }

    @Override
    public RegularMeetingVO getMeetingById(int meetingId, int userIdx) {
        return regularMeetingMapper.selectMeetingById(meetingId, userIdx);
    }

    @Override
    public void updateMeeting(RegularMeetingVO meeting) {
        RegularMeetingVO existingMeeting = regularMeetingMapper.selectMeetingById(meeting.getMeeting_idx(),
                meeting.getLeader_idx());
        if (existingMeeting == null || existingMeeting.getLeader_idx() != meeting.getLeader_idx()) {
            throw new IllegalStateException("Only the leader can update this meeting");
        }
        regularMeetingMapper.updateMeeting(meeting);
    }

    @Override
    public void deleteMeeting(int meetingIdx, int leaderIdx) {
        // 기존 모임 가져와서 leader 권한 확인
        RegularMeetingVO vo = new RegularMeetingVO();
        vo.setMeeting_idx(meetingIdx);
        vo.setLeader_idx(leaderIdx);
        regularMeetingMapper.deleteMeeting(vo);
    }

    @Override
    public boolean isFavorite(int userIdx, int meetingId) {
        return regularMeetingMapper.checkFavorite(userIdx, meetingId);
    }

    @Override
    @Transactional
    public boolean toggleFavorite(int userIdx, int meetingId) {
        boolean isFavorite = regularMeetingMapper.checkFavorite(userIdx, meetingId);
        if (isFavorite) {
            regularMeetingMapper.deleteFavorite(userIdx, meetingId);
            return false;
        } else {
            regularMeetingMapper.insertFavorite(userIdx, meetingId);
            return true;
        }
    }

    @Override
    public boolean doesHashtagExist(String hashtag) {
        return regularMeetingMapper.findHashtagByName(hashtag) > 0;
    }

    @Override
    public int findHashtagByName(String hashtag) {
        return regularMeetingMapper.findHashtagByName(hashtag);
    }

    @Override
    public List<HashtagVO> getAllHashtags() {
        return regularMeetingMapper.selectAllHashtags();
    }

    @Override
    public void insertMeetingHashtags(int meetingIdx, int hashtagIdx) {
        regularMeetingMapper.insertMeetingHashtags(meetingIdx, hashtagIdx);
    }

    @Override
    public List<Map<String, Object>> getMeetingMembersProfile(int meetingIdx) {
        return regularMeetingMapper.selectMeetingMembersProfile(meetingIdx);
    }

    @Override
    public void joinMeeting(int meetingId, int userIdx) {
        // 1) 이미 가입된 유저인지 체크
        boolean alreadyMember = regularMeetingMapper.isMember(meetingId, userIdx);
        if (alreadyMember) {
            // 이미 가입 상태이므로, INSERT 하지 말고 예외 처리 or 단순 메시지 반환
            throw new IllegalStateException("이미 가입된 회원입니다.");
            // 혹은 return 해서 가입 스킵
        }
        // 2) 가입 진행
        regularMeetingMapper.joinMeeting(meetingId, userIdx);
    }

    @Override
    public boolean isMember(int meetingId, int userIdx) {
        return regularMeetingMapper.isMember(meetingId, userIdx);
   
}

@Override
@Transactional
public void leaveMeeting(int meetingId, int userIdx) {
    boolean isMember = regularMeetingMapper.isMember(meetingId, userIdx);
    if (!isMember) {
        throw new IllegalStateException("해당 모임의 멤버가 아닙니다.");
    }
    // 모임 탈퇴 처리
    regularMeetingMapper.leaveMeeting(meetingId, userIdx);
}

@Override
public void admindeleteMeeting(int meetingIdx) {
    regularMeetingMapper.admindeleteMeeting(meetingIdx);
}
}