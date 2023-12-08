package com.ex.laos;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ex.laos.eqpmnt.service.EqpmntService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RoutingController {

	private final EqpmntService eqpmntService;

	@GetMapping("/login")
	public ModelAndView loginPage(){
		return new ModelAndView("mngmt/login");
	}

	@GetMapping("/join")
	public ModelAndView joinUsPage(){
		return new ModelAndView("mngmt/join");
	}

	@GetMapping("/home")
	public ModelAndView home() { return new ModelAndView("home"); }

	@GetMapping("/update/pwd")
	public ModelAndView updatePassword(){ return new ModelAndView("mngmt/updatePwd"); }

	@GetMapping("/update/auth")
	public ModelAndView updateAuth(){ return new ModelAndView("mngmt/updateAuth"); }

	@GetMapping("/realtime/equipment/inspection/history")
	public ModelAndView equipmentInspectionHistory(){
		ModelAndView modelAndView = new ModelAndView("eqpmnt/eqpmnt-Inspection-hstry");
		modelAndView.addObject("list", eqpmntService.selectEqpmntInspectionHistoryList());
		return modelAndView;
	}

	@GetMapping("/realtime/equipment/inspection/item")
	public ModelAndView equipmentInspectionItem(){
		ModelAndView modelAndView = new ModelAndView("eqpmnt/eqpmnt-Inspection-itm");
		modelAndView.addObject("list", eqpmntService.selectEqpmntInspectionItemList());
		return modelAndView;
	}
}
