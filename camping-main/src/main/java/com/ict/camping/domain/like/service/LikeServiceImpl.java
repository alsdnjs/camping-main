package com.ict.camping.domain.like.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.domain.like.mapper.LikeMapper;
import com.ict.camping.domain.like.vo.LikeVO;

@Service
public class LikeServiceImpl implements LikeService{
    @Autowired LikeMapper likeMapper;

    @Override
    public int checkLike(LikeVO lvo) {
        return likeMapper.checkLike(lvo); 
    }

    @Override
    public int insertLike(LikeVO lvo) {
        return likeMapper.insertLike(lvo);
    }

    @Override
    public int deleteLike(LikeVO lvo) {
        return likeMapper.deleteLike(lvo);
    }

}
