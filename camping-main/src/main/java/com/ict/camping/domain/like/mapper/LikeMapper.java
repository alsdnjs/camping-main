package com.ict.camping.domain.like.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.domain.like.vo.LikeVO;

@Mapper
public interface LikeMapper {
    int checkLike(LikeVO lvo);
    int insertLike(LikeVO lvo);
    int deleteLike(LikeVO lvo);
}
