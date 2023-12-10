package com.ex.laos.eqpmnt.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EqpmntInspectionItem {
	private String chckArtclId;
	private String chckArtclNm;
	private String chckArtclGuide;
	private String obsvtrType;
	private String regDt;
	private String rgtrNm;
	private String mdfcnDt;
	private String mdfrId;

	private String useYN;
}
