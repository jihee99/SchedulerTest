package com.ex.laos.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DamController {

	@Scheduled(cron = "0 0,30 9-12,14-17 * * *")
	public void CronTest(){

		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");



	}





}
