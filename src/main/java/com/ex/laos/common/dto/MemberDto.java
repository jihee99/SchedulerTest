package com.ex.laos.common.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberDto {

	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String mbrId;


	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	// @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
	private String pswd;

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String nm;

	private String telno;

	@NotEmpty(message = "소속기관명은 필수 입력 값입니다.")
	private String ogdp;		// 소속

	private String stts;

	private Date regDt;

	private Date mdfcnDt;

	private String mdfrId;

	private String sevcAuthrtId;	// 서비스 권한


// 	@Builder
// 	public MemberFormDto toEntity(String mbrId, String pswd, String nm, String telno, String ogdp, String stts, Date regDt, String sevcAuthrtId){
// 		this.mbrId = mbrId;
// 		this.pswd = pswd;
// 		this.nm = nm;
// 		this.telno = telno;
// 		this.ogdp = ogdp;
// 		this.stts = stts;
// 		this.regDt = regDt;
// 		this.sevcAuthrtId = sevcAuthrtId;
// 	}


}
