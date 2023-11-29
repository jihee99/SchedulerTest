package com.ex.laos.dam.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/update3/{selected}")
	public void uploadOldDataToDatabase(@PathVariable("selected") String selected) {
		damService.uploadOldDataToDatabase(selected);
	}

}
