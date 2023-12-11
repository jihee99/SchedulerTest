package com.ex.laos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String updatePassword(){
		// return new ModelAndView("mngmt/updatePwd");
	return "mngmt/updatePwd";
	}

	@GetMapping("/update/auth")
	public String updateAuth(){
		// return new ModelAndView("mngmt/updateAuth");
		return "mngmt/updateAuth";
	}

	@GetMapping("/realtime/equipment/inspection/history")
	public String equipmentInspectionHistory(Model model){
		// ModelAndView modelAndView = new ModelAndView("eqpmnt/eqpmnt-Inspection-hstry");
		// modelAndView.addObject("list", eqpmntService.selectEqpmntInspectionHistoryList());

		model.addAttribute("list", eqpmntService.selectEqpmntInspectionHistoryList());
		return "eqpmnt/eqpmnt-Inspection-hstry";
	}

	@GetMapping("/realtime/eqpmnt/get/hstry/search")
	public String selectEqpmntInspectionHistorySearchList(
		@RequestParam("type") String type,
		@RequestParam("station") String station,
		@RequestParam("datefilter") String period
		, Model model
	){
		// Map<String, Object> response = new HashMap<>();
		// List<Map<String, String>> list = eqpmntService.selectEqpmntInspectionHistorySearchList(type, station, period);
		// if(!list.isEmpty()){
		// 	response.put("status", "success");
		// 	response.put("list", list);
		// }else{
		// 	response.put("status", "error");
		// 	response.put("message", "점검 이력 조회에 실패했습니다.");
		// }
		// return response;
		model.addAttribute("list", eqpmntService.selectEqpmntInspectionHistorySearchList(type, station, period));
		return "eqpmnt/eqpmnt-inspection-hstry :: tableFragment";
	}


	@GetMapping("/realtime/equipment/inspection/item")
	public String equipmentInspectionItem(Model model){
		// ModelAndView modelAndView = new ModelAndView("eqpmnt/eqpmnt-Inspection-itm");
		// modelAndView.addObject("list", eqpmntService.selectEqpmntInspectionItemList());
		// return modelAndView;
		model.addAttribute("list", eqpmntService.selectEqpmntInspectionItemList());
		return "eqpmnt/eqpmnt-inspection-itm";
	}

	@GetMapping("/realtime/eqpmnt/items/search")
	public String insertEqpmntInspectionArtcl(
		@RequestParam("type") String type,
		Model model
	){
		model.addAttribute("list", eqpmntService.selectEqpmntInspectionItemListByType(type));
		return "eqpmnt/eqpmnt-inspection-itm :: tableFragment";
	}


}
