package com.ex.laos.dmh.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.cglib.core.Local;

import lombok.Data;

@Data
public class DmhObservationData {
	private String obsvtrId;

	private Double obsVal;
	private String observationDate;

	private Double meanMinTemp;
	private Double meanMaxTemp;

	public void setObservationDate(String observationDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime adjusted =  LocalDate.parse(observationDate, formatter).atStartOfDay();

		this.observationDate = adjusted.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}


	public void setObsVal(BigDecimal asNumber) {
		this.obsVal = convertBigDecimalToDouble(asNumber);
	}

	public void setMeanMinTemp(BigDecimal asNumber) {
		this.meanMinTemp = convertBigDecimalToDouble(asNumber);
	}

	public void setMeanMaxTemp(BigDecimal asNumber) {
		this.meanMaxTemp = convertBigDecimalToDouble(asNumber);
	}

	public Double convertBigDecimalToDouble(BigDecimal bigDecimalValue) {
		if (bigDecimalValue != null) {
			return bigDecimalValue.doubleValue();
		}
		return null;
	}

}
