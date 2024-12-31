package com.ict.camping.domain.reqularMeeting.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ict.camping.domain.reqularMeeting.vo.HashtagVO;
import com.ict.camping.domain.reqularMeeting.vo.ReqularMeetingVO;

public interface ReqularMeetingService {
  int createMeeting(ReqularMeetingVO meeting, List<HashtagVO> hashtagList);
  ReqularMeetingVO selectMeetingById(int meetingId);
  List<Map<String, Object>> selectAllMeetings(int user_idx);

  // 해시태그 관련
  int findOrCreateHashtag(String hashtag);
  void insertMeetingHashtags(int meetingIdx, int hashtagIdx);
  List<HashtagVO> selectAllHashtags();

  boolean toggleFavorite(int userIdx, int meetingIdx);
  List<Map<String, Object>> getMeetingMembersProfile(int meetingId);
  Integer findHashtagByName(String trimTag);
}
