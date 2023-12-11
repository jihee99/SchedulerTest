package com.ex.laos.eqpmnt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.dam.service.APIService;
import com.ex.laos.eqpmnt.dto.EqpmntInspectionDto;
import com.ex.laos.eqpmnt.service.EqpmntService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/realtime/eqpmnt")
public class EqpmntController {

	private final EqpmntService eqpmntService;

	@GetMapping("/get/hstry")
	public Map<String, Object> selectEqpmntInspectionHistoryList(){

		Map<String, Object> response = new HashMap<>();

		List<Map<String, String>> list = eqpmntService.selectEqpmntInspectionHistoryList();
		if(!list.isEmpty()){
			response.put("status", "success");
			response.put("list", list);
		}else{
			response.put("status", "error");
			response.put("message", "점검 이력 조회에 실패했습니다.");
		}

		return response;
	}

	@PostMapping("/get/hstry/details")
	public Map<String, Object> selectEqpmntInspectionDetailsByHstryId(
		@RequestParam("hstryCode") String hstryCode
	){
		Map<String, Object> response = new HashMap<>();


		Map<String, Object> data = eqpmntService.selectEqpmntInspectionDetailsByHstryId(hstryCode);

		if (!data.containsKey("error")) {
			response.put("status", "success");
			response.put("data", data);
		}else{
			response.put("status", "error");
			response.put("message", "점검 결과가 존재하지 않습니다.");
		}
		return response;
	}

	// @GetMapping("/get/hstry/search")
	// public Map<String, Object> selectEqpmntInspectionHistorySearchList(
	// 	@RequestParam("type") String type,
	// 	@RequestParam("station") String station,
	// 	@RequestParam("datefilter") String period
	// 	// , Model model
	// ){
	// 	Map<String, Object> response = new HashMap<>();
	//
	// 	List<Map<String, String>> list = eqpmntService.selectEqpmntInspectionHistorySearchList(type, station, period);
	// 	if(!list.isEmpty()){
	// 		response.put("status", "success");
	// 		response.put("list", list);
	// 	}else{
	// 		response.put("status", "error");
	// 		response.put("message", "점검 이력 조회에 실패했습니다.");
	// 	}
	// 	// model.addAttribute("list",  eqpmntService.selectEqpmntInspectionHistorySearchList(type, station, period));
	// 	// return "eqpmnt/eqpmnt-Inspection-hstry :: #tableFragment";
	// 	return response;
	// }

	@PostMapping("/items/register")
	public Map<String, String> insertEqpmntInspectionArtcl(
		@ModelAttribute EqpmntInspectionDto dto
	){
		Map<String, String> response = new HashMap<>();
		try{
			eqpmntService.insertEqpmntInspectionArtcl(dto);
			response.put("status", "success");
			response.put("message", "등록이 완료되었습니다.");
		}catch (Exception e) {
			response.put("status", "error");
			response.put("message", "등록에 실패했습니다. 관리자에게 문의해주세요.");
		}
		return response;
	}

	@GetMapping("/items/details")
	public Map<String, Object> insertEqpmntInspectionArtclDetailById(
		@RequestParam("artclId") String artclId
		// Model model
	){
		Map<String, Object> response = new HashMap<>();
		try{
			response.put("status", "success");
			response.put("data", eqpmntService.selectEqpmntInspectionItemDetailById(artclId));
		}catch (Exception e) {
			response.put("status", "error");
			response.put("message", "조회에 실패했습니다. 관리자에게 문의해주세요.");
		}

		return response;
		// model.addAttribute("details", eqpmntService.selectEqpmntInspectionItemDetailById(artclId));
		// return "eqpmnt/eqpmnt-inspection-itm :: detailModalBody";
	}
}
