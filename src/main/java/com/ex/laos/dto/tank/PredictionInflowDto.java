package com.ex.laos.dto.tank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredictionInflowDto {
	private String date;
	private String RMm;   //강우
	private String QoCms;  //관측
	private String QsCms;  //계산치
}
