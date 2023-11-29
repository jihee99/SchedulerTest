package com.ex.laos.tank.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionSummaryDto {

	private String tankSimId;			// 시뮬레이션 아이디
	private String damId;				// 댐 아이디
	private String predictionBeginYmd;	// 예측 시작일
	private String predictionEndYmd;	// 예측 종료일

	private String realRainfall;
	private String observedFlowDept;
	private String computedFlowDept;
	private String evapotranspiration;
	private String ratio;

	private String obsMean;
	private String obsSdev;
	private String simMean;
	private String simSdev;

	/**
	 * 분리 할지 말지 고민해보기
	 * */
	private ArrayList<PredictionInflowDto> inflows;
}
