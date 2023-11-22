package com.ex.laos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tank")
public class TankController {

	@GetMapping("")
	public ModelAndView tankPage() {
		ModelAndView mav = new ModelAndView("tank2");
		return mav;
	}

}
