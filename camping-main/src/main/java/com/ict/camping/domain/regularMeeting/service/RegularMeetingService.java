package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.vo.HashtagVO;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingVO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface RegularMeetingService {

    // 정규 모임 생성
    int createMeeting(RegularMeetingVO meeting, List<String> hashtags);

    // 전체 정규 모임 목록 가져오기
    List<RegularMeetingVO> getAllMeetings(int userIdx);

    // 특정 정규 모임 조회
    RegularMeetingVO getMeetingById(int meetingId, int userIdx);

    // 정규 모임 업데이트
    void updateMeeting(RegularMeetingVO meeting);

    // 정규 모임 삭제
    void deleteMeeting(int meetingIdx, int leaderIdx);

    // 좋아요 상태 확인
    boolean isFavorite(int userIdx, int meetingId);

    // 좋아요 토글
    boolean toggleFavorite(int userIdx, int meetingId);

    // 해시태그 존재 여부 확인
    boolean doesHashtagExist(String hashtag);

    // 해시태그 이름으로 해시태그 아이디 찾기
    int findHashtagByName(String hashtag);

    // 해시태그 목록 가져오기
    List<HashtagVO> getAllHashtags();

    // 모임-해시태그 연결 삽입
    void insertMeetingHashtags(int meetingIdx, int hashtagIdx);

    // 모임 가입 멤버 프로필 가져오기
    List<Map<String, Object>> getMeetingMembersProfile(int meetingIdx);

    // 모임 가입
    void joinMeeting(@Param("meetingId") int meetingId, @Param("userIdx") int userIdx);

    // 가입 멤버 확인
    boolean isMember(@Param("meetingId") int meetingId, @Param("userIdx") int userIdx);

    // 모임 탈퇴
    void leaveMeeting(@Param("meetingId") int meetingId, @Param("userIdx") int userIdx);

    // <관리자> 정규 모임 삭제
    void admindeleteMeeting(int meetingIdx);
}