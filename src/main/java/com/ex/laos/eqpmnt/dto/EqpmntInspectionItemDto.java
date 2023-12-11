package com.ex.laos.eqpmnt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EqpmntInspectionItemDto {
	private String chckArtclId;
	private String chckArtclNm;
	private String chckArtclGuide;
	private String obsvtrType;
	private String regDt;
	private String rgtrNm;
	private String rgtrId;
	private String mdfcnDt;
	private String mdfrId;

	private String useYN;
}
