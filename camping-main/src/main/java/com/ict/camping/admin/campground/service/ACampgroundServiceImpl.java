package com.ict.camping.admin.campground.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.admin.campground.mapper.ACampgroundMapper;
import com.ict.camping.admin.campground.vo.ACampgroundVO;
@Service
public class ACampgroundServiceImpl implements ACampgroundService{

    @Autowired
    private ACampgroundMapper campgroundMapper;

    // 모든 캠핑장 데이터를 가져오는 메서드
    @Override
    public List<ACampgroundVO> getAllCampingSites() {
        return campgroundMapper.findAllCampgroundNotice();
    }

    @Override
    public ACampgroundVO findCampingSiteByContentId(String contentId) {
        return campgroundMapper.findCampgroundNoticeById(contentId);
    }

    @Override
    public int insertCampingSite(ACampgroundVO mapVO) {
        return campgroundMapper.insertCampgroundNotice(mapVO);
    }

    @Override
    public void updateRequestAnswer(int campRequestIdx, String requestAnswer) {
        campgroundMapper.updateRequestAnswer(campRequestIdx, requestAnswer);
    }


}