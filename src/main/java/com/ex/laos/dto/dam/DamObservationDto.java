package com.ex.laos.dto.dam;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class DamObservationDto {

	private String damId;
	private String obsrvnYmd;
	private String wl;			// Water Level(masl)
	private String vol;			// Volum (MCM)
	private String inflow;		// Inflow (cms)
	private String pg;			// Power (MW)
	private String fsp;			// Flow Spill (cms)
	private String fg;			// Flow Gen(cms)
	private String fto;			// Flow Throught Other (cms)
	private String tofl;		// Total Outflow (cms)
	private String twl;			// Tail Water Level (masl)
	private String rf;			// Rain (mm)


	@Override
	public String toString() {
		return "DamObservationDto{" +
			"damId='" + damId + '\'' +
			", obsrvnYmd='" + obsrvnYmd + '\'' +
			", wl='" + wl + '\'' +
			", vol='" + vol + '\'' +
			", inflow='" + inflow + '\'' +
			", pg='" + pg + '\'' +
			", fsp='" + fsp + '\'' +
			", fg='" + fg + '\'' +
			", fto='" + fto + '\'' +
			", tofl='" + tofl + '\'' +
			", twl='" + twl + '\'' +
			", rf='" + rf + '\'' +
			'}';
	}

}
