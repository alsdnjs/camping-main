package com.ict.camping.domain.like.service;

import com.ict.camping.domain.like.vo.LikeVO;

public interface LikeService {
    int checkLike(LikeVO lvo);
    int insertLike(LikeVO lvo);
    int deleteLike(LikeVO lvo);
}
