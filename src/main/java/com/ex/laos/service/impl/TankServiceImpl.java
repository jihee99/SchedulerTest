package com.ex.laos.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ex.laos.dto.tank.PredictionSummaryDto;
import com.ex.laos.service.TankService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TankServiceImpl implements TankService {

	@Override
	public PredictionSummaryDto generateTankInputFile(MultipartFile file, String floatingSelect) throws
		InterruptedException {
		return null;
	}

	@Override
	public PredictionSummaryDto generateTankInputFile2(String floatingSelect) throws InterruptedException {
		return null;
	}

	@Override
	public PredictionSummaryDto readEstimateInflowModelResult(String fileName, String floatingSelect) {
		return null;
	}

	@Override
	public void downloadTemplateFileByDam(HttpServletResponse response, String name) {

	}
}
