// package com.ex.laos.controller;
//
// import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.ex.laos.dam.service.BatchService;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @RestController
// @RequiredArgsConstructor
// @CrossOrigin("*")
// @EnableAutoConfiguration
// @Component
// public class BatchController {
//
// 	private final BatchService batchService;
//
// 	@Scheduled(cron="${batch.1m.crond}")
// 	public void t1mChkAPIDownload(){
// 		log.info("{}", "testTime");
// 	}
//
// 	@Scheduled(cron="${batch.6h.crond}")
// 	public void t6hChkAPIDownload(){
// 		log.info("{}", "testTime");
// 	}
//
// }
