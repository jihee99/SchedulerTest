package com.ex.laos.dam.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.dam.service.APIService;
import com.ex.laos.dam.service.DamService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/damtest")
public class TestController {

	private final DamService damService;

	@GetMapping("/update1/{selected}")
	public void registerAllData(@PathVariable("selected") String selected) {
		damService.registerAllData(selected);
	}

	// @GetMapping("/update2/{selected}")
	// public void upsertLastFiveDaysData(@PathVariable("selected") String selected){
	// 	int previousDays = 5;
	// 	damService.uploadPreviousData(selected, previousDays);
	// }

	// @GetMapping("/update3/{selected}")
	// public void uploadOldDataToDatabase(@PathVariable("selected") String selected) {
	// 	damService.uploadOldDataToDatabase(selected);
	// }

	// @GetMapping("/google/api/download")
	// public void cronTest() {
	// 	// 테스트용 코드
	// 	LocalDateTime localDateTime = LocalDateTime.now();
	// 	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	// 	System.out.println("test : " + localDateTime.format(dateTimeFormatter));
	//
	// 	int previousDays = 5;
	// 	try {
	// 		// API 서비스 호출
	// 		String filePath = apiService.apiExcelFileDownload();
	// 		if (Files.exists(Paths.get(filePath))) {
	// 			// 파일이 존재하면 uploadPreviousData 서비스 호출
	// 			damService.uploadPreviousData(filePath, "NamNgum1", previousDays);
	// 			log.info("File exists and uploaded successfully. file Path: {}", filePath);
	// 		} else {
	// 			log.info("File does not exist at: {}", filePath);
	//
	// 		}
	// 	} catch (Exception e) {
	//
	// 		// 실패 시 로그에 에러 메시지 찍기
	// 		log.error("Cron Test Failed: "+e.getMessage());
	// 	}
	// }

}
