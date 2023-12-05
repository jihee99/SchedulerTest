package com.ex.laos.common.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class MailTokenDto {

	private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 24 * 60;
	// 24시간을 분으로

	private String pswdChgId;
	private String mbrId;
	private LocalDateTime regDt;
	private LocalDateTime expryDt;
	private LocalDateTime chgDt;
	private char pswdChgYn;

	private String rawPwd;
	private String newPwd;
	private String newPwdChk;

	public static MailTokenDto createEmailId(String username, String pswdChgId) {
		MailTokenDto emailToken = new MailTokenDto();
		emailToken.pswdChgId = pswdChgId;
		emailToken.mbrId = username;
		emailToken.regDt = LocalDateTime.now();
		emailToken.expryDt = emailToken.regDt.plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE);
		emailToken.pswdChgYn = 'N';

		return emailToken;
	}

	public void setTokenToUsed() {
		this.pswdChgYn = 'Y';
	}

}
