package com.ex.laos.dto.tank;

import java.util.ArrayList;

import com.ex.laos.dto.EstimateInflowVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstimateSummaryDto {

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
	private ArrayList<EstimateInflowVo> inflows;
}
