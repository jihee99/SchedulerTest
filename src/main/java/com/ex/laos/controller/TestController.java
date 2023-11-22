package com.ex.laos.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestController {

	@Scheduled(cron = "0 5 * * * *")
	public void CronTest(){
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("test : " + localDateTime.format(dateTimeFormatter) );
	}
}
