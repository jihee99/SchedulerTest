package com.ex.laos.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.ex.laos.dto.tank.EstimateSummaryDto;

public interface TankService {

	EstimateSummaryDto generateTankInputFile(MultipartFile file, String floatingSelect) throws InterruptedException;
	EstimateSummaryDto generateTankInputFile2(String floatingSelect) throws InterruptedException;

	EstimateSummaryDto readEstimateInflowModelResult(String fileName, String floatingSelect);

	void downloadTemplateFileByDam(HttpServletResponse response, String name);


}
