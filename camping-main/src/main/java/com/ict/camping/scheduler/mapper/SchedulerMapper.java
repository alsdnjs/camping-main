package com.ict.camping.scheduler.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulerMapper {
    
    // 체크아웃 날짜가 지나면 예약을 '이용'으로 상태 변경
    void updateExpiredReservations();
}
