package com.ex.laos.dto.tank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EstimateInflowDto {
	private String date;
	private String RMm;   //강우
	private String QoCms;  //관측
	private String QsCms;  //계산치
}
