package com.ex.laos.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.ex.laos.dto.tank.PredictionSummaryDto;

public interface TankService {

	PredictionSummaryDto generateTankInputFile(MultipartFile file, String floatingSelect) throws InterruptedException;
	PredictionSummaryDto generateTankInputFile2(String floatingSelect) throws InterruptedException;

	PredictionSummaryDto readEstimateInflowModelResult(String fileName, String floatingSelect);

	void downloadTemplateFileByDam(HttpServletResponse response, String name);

	String formatData(String data);

}
