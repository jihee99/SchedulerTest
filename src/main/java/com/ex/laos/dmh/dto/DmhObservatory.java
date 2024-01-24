package com.ex.laos.dmh.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class DmhObservatory {

	private String obsvtrId;
	private String obsvtrNm;

	private String obsvtrType;

	LocalDate psnDataBgngYmd;
	LocalDate psnDataEndYmd;

	public void setPsnDataBgngYmd(String psnDataBgngYmd) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.psnDataBgngYmd = LocalDate.parse(psnDataBgngYmd, formatter);
	}

	public void setPsnDataEndYmd(String psnDataBgngYmd) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.psnDataBgngYmd = LocalDate.parse(psnDataBgngYmd, formatter);
	}
}
