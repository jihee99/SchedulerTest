package com.ex.laos.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ex.laos.dto.tank.EstimateSummaryDto;
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
		ModelAndView mav = new ModelAndView("tank2");
		return mav;
	}

	@PostMapping("/run1")
	public EstimateSummaryDto runTank(@RequestPart MultipartFile file, @RequestParam("floatingSelect") String floatingSelect) throws InterruptedException {
		if (!file.isEmpty()) {
			log.info("{}", floatingSelect);
			EstimateSummaryDto es = tankService.generateTankInputFile(file, floatingSelect);
			return es;
		}else{
			log.info("File does not exist");
			return null;
		}
	}

	@PostMapping("/run2")
	public EstimateSummaryDto runTank(@RequestParam("floatingSelect") String floatingSelect) throws InterruptedException {
		log.info("{}", floatingSelect);
		EstimateSummaryDto es = tankService.generateTankInputFile2(floatingSelect);
		return es;
	}

}
