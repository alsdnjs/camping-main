package com.ict.camping.domain.myPage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.domain.myPage.vo.CampingSiteVO;
import com.ict.camping.domain.myPage.vo.UsageHistoryVO;


@Mapper
public interface MyPageMapper {
    List<String> getMyFavoriteCampingSites(String user_idx);
    List<CampingSiteVO> getCampingSitesById(List<String> contentIds);
    
    int deleteMyCampingSite(Map<String, String> map);

    List<UsageHistoryVO> getUsageHistory(String user_idx);
}
