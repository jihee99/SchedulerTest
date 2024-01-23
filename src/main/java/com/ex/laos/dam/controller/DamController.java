package com.ex.laos.dam.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ex.laos.dam.service.APIService;
import com.ex.laos.dam.service.DamService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DamController {

	private final APIService apiService;
	private final DamService damService;
	@Autowired
	public DamController(APIService apiService, DamService damService) {
		this.apiService = apiService;
		this.damService = damService;
	}


	@Scheduled(cron = "0 0 9,11,14,16 * * *")
	public void cronTest() {
		// 테스트용 코드
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("test : " + localDateTime.format(dateTimeFormatter));

		int previousDays = 5;
		try {
			// API 서비스 호출
			String filePath = apiService.apiExcelFileDownload();
			if (Files.exists(Paths.get(filePath))) {
				// 파일이 존재하면 uploadPreviousData 서비스 호출
				// damService.uploadPreviousData(filePath, "NamNgum1", previousDays);
				log.info("File exists and uploaded successfully. file Path: {}", filePath);
			} else {
				log.info("File does not exist at: {}", filePath);

			}
		} catch (Exception e) {

			// 실패 시 로그에 에러 메시지 찍기
			log.error("Cron Test Failed: "+e.getMessage());
		}
	}


	// @Scheduled(cron = "1 * * * * ?")
	// public void CronTest(){
	//
	//
	//
	// 	apiService.apiExcelFileDownload();
	//
	// }

	// @Scheduled(cron = "0 0,30 9-12,14-17 * * *")
	// public void CronTest2(){
	//
	// 	LocalDateTime localDateTime = LocalDateTime.now();
	// 	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	// 	System.out.println("test : " + localDateTime.format(dateTimeFormatter));
	//
	//
	//
	// }




}
