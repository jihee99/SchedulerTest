package com.ex.laos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ex.laos.dto.tank.PredictionSummaryDto;
import com.ex.laos.service.TankService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tank")
public class TankController {

	private final TankService tankService;

	@GetMapping("")
	public ModelAndView tankPage() {
		return new ModelAndView("tank2");
	}

	@PostMapping("/run1")
	public PredictionSummaryDto runTank(@RequestPart MultipartFile file, @RequestParam("floatingSelect") String floatingSelect) throws InterruptedException {
		if (!file.isEmpty()) {
			log.info("{}", floatingSelect);
			PredictionSummaryDto es = tankService.generateTankInputFile(file, floatingSelect);
			return es;
		}else{
			log.info("File does not exist");
			return null;
		}
	}

	@PostMapping("/run2")
	public PredictionSummaryDto runTank(@RequestParam("floatingSelect") String floatingSelect) throws InterruptedException {
		log.info("selected : {}", floatingSelect);
		PredictionSummaryDto es = tankService.generateTankInputFile2(floatingSelect);
		return es;
	}

}
