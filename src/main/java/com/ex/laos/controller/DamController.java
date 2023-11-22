package com.ex.laos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.service.DamService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dam")
public class DamController {


	private final DamService damService;

	@GetMapping("/update1/{selected}")
	public void registerAllData(@PathVariable("selected") String selected) {
		damService.registerAllData(selected);
	}

	@PostMapping("/update2/{selected}")
	public void upsertLastFiveDaysData(@PathVariable("selected") String selected){
		damService.upsertLastFiveDaysData(selected);
	}
}
