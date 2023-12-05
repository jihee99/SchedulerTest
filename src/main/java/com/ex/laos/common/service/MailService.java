package com.ex.laos.common.service;

import javax.mail.MessagingException;

public interface MailService {

	void sendEmailForPasswordReset(String username) throws MessagingException ;

	void updatePassword(String username, String rawPassword);

}
