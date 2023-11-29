package com.ex.laos.tank.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.ex.laos.tank.dto.PredictionSummaryDto;

public interface TankService {

	PredictionSummaryDto generateTankInputFile(MultipartFile file, String floatingSelect) throws InterruptedException;
	PredictionSummaryDto generateTankInputFile2(String floatingSelect) throws InterruptedException;

	void downloadTemplateFileByDam(HttpServletResponse response, String name);

	String formatData(String data);

}
