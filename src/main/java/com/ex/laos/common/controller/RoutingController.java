package com.ex.laos.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoutingController {

	@GetMapping("/login")
	public ModelAndView loginPage(){
		return new ModelAndView("login");
	}

	@GetMapping("/join")
	public ModelAndView joinUsPage(){
		return new ModelAndView("join");
	}

	@GetMapping("/home")
	public ModelAndView home() { return new ModelAndView("home"); }

	@GetMapping("/update/pwd")
	public ModelAndView updatePassword(){ return new ModelAndView("updatePwd"); }

	@GetMapping("/update/auth")
	public ModelAndView updateAuth(){ return new ModelAndView("updateAuth"); }


}
