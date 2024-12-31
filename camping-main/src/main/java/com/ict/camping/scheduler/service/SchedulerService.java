package com.ict.camping.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ict.camping.scheduler.mapper.SchedulerMapper;

@Service
public class SchedulerService {

    @Autowired
    private SchedulerMapper schedulerMapper;

    // 하루에 한 번 실행 (매일 자정에 체크하여 '예약' 상태를 '이용'으로 변경)
    @Scheduled(cron = "0 0 * * * ?") // cron 표현식: 매일 자정 (00:00:00) 실행
    public void updateExpiredReservations() {
        schedulerMapper.updateExpiredReservations();
        System.out.println("체크아웃 날짜가 지난 예약을 '이용' 상태로 변경");
    }
}
