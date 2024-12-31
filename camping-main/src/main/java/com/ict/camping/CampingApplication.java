package com.ict.camping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링 기능 
public class CampingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CampingApplication.class, args);
	}

}
	