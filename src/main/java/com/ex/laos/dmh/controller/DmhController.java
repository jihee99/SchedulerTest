package com.ex.laos.dmh.controller;

import java.io.FileNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ex.laos.dmh.service.DmhFileParsingService;
import com.ex.laos.dmh.service.DmhObservationDataParsingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DmhController {

	private final DmhFileParsingService dmhFileParsingService;
	private final DmhObservationDataParsingService dmhObservationDataParsingService;

	// @GetMapping("/t/dmh-file-parsing/{pathIdx}")
	// public String dmhFileParsing(@PathVariable("pathIdx") int pathIdx) {
	// 	dmhFileParsingService.dmhEvaporationFileParsing(pathIdx);
	//
	//
	// 	return "/t/equipment-list";
	// }

	@PostMapping("/peto/dmh-file-parsing/{type}/{obsvtr}")
	public String parseObservationData(@PathVariable("type") String type, @PathVariable("obsvtr") String obsvtrNm) {
		try {
			dmhObservationDataParsingService.parseObservationData(type, obsvtrNm);
			System.out.println("11111111111111");
		} catch (FileNotFoundException e) {
			System.out.println("222222222222222");
			log.info("{}","data parsing error!!");
		}
		return "/home";
	}
}
