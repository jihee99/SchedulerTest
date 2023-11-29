package com.ex.laos.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto implements Serializable {

	private String mbrId;
	private String pswd;
	private String nm;
	private String telno;
	private String ogdp;		// 소속
	private String stts;
	private Date regDt;
	private Date mdfcnDt;
	private String mdfrId;

	private String sevcAuthrtId;	// 서비스 권한

}
