package com.ex.laos.common.service;

import javax.mail.MessagingException;

import com.ex.laos.common.dto.MailTokenDto;

public interface MailService {

	void sendEmailForPasswordReset(String username) throws MessagingException ;

	void updatePassword(String username, String rawPassword);

	MailTokenDto isTokenValid(String token);
}
