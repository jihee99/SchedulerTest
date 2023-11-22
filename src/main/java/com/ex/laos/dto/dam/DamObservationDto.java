package com.ex.laos.dto.dam;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class DamObservationDto {

	private String damId;
	private String obsrvnYmd;
	private BigDecimal wl;			// Water Level(masl)
	private BigDecimal vol;			// Volum (MCM)
	private BigDecimal inflow;		// Inflow (cms)
	private BigDecimal pg;			// Power (MW)
	private BigDecimal fsp;			// Flow Spill (cms)
	private BigDecimal fg;			// Flow Gen(cms)
	private BigDecimal fto;			// Flow Throught Other (cms)
	private BigDecimal tofl;		// Total Outflow (cms)
	private BigDecimal twl;			// Tail Water Level (masl)
	private BigDecimal rf;			// Rain (mm)


	@Override
	public String toString() {
		return "DamObservationDto{" +
			"damId='" + damId + '\'' +
			", obsrvnYmd='" + obsrvnYmd + '\'' +
			", wl=" + wl +
			", vol=" + vol +
			", inflow=" + inflow +
			", pg=" + pg +
			", fsp=" + fsp +
			", fg=" + fg +
			", fto=" + fto +
			", tofl=" + tofl +
			", twl=" + twl +
			", rf=" + rf +
			'}';
	}

}
