package com.ex.laos.common.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.laos.common.dao.MailDao;
import com.ex.laos.common.dto.MailTokenDto;
import com.ex.laos.common.service.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

	private final JavaMailSender javaMailSender;
	private final MailDao mailDao;


	@Override
	@Transactional
	public void sendEmailForPasswordReset (String username) throws MessagingException {

		String pswdChgId = UUID.randomUUID().toString();
		MailTokenDto mailTokenDto = MailTokenDto.createEmailId(username, pswdChgId);

		System.out.println("EmailToken >> " + mailTokenDto);

		// 1. 비밀번호 변경 이력 테이블에 insert
		mailDao.insertPasswordUpdateHistory(mailTokenDto);

		// 2. 이메일 전송
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper =  new MimeMessageHelper(message, true);

		helper.setTo(mailTokenDto.getMbrId());
		helper.setSubject("LaoWIS 비밀번호 재설정 안내 이메일입니다.");

		String url = "http://localhost:9900/password/reset?token=" + mailTokenDto.getPswdChgId();
		String html = setEmailHtmlWithButton(url);
		message.setText(html, "utf-8", "html");

		javaMailSender.send(message);
	}

	@Override
	@Transactional
	public void updatePassword(String username, String rawPassword) {
		Map<String, String> userdata = new HashMap<>();
		userdata.put("username", username);
		userdata.put("pswdChgId", UUID.randomUUID().toString());

		// 1. 비밀번호 변경 이력 테이블에 insert
		// mailDao.insertPasswordUpdateHistory(userdata);

		// 2. 사용자 정보 update
		// String updatePwd = passwordEncoder.encode(rawPassword);
		// System.out.println(updatePwd);
		// mailDao.updatePasswordByUsername(username, updatePwd);
	}

	private String setEmailHtmlWithButton(String link) {
		return  "<html><head><style>" +
				".container { font-family: Arial, sans-serif; font-size: 13px; text-align: center; }" +
				"a,a:visited { text-decoration: none; color: #00AE68;}" +
				"a.button { display: inline-block; width: 120px; padding: 0; margin: 50px 10px 10px 0; font-weight: 600; text-align: center; line-height: 40px; color: #FFF; border-radius: 5px; transition: all 0.2s; }" +
				".realButton { background: #9f9f9f; }" +
				".btnPush:hover { margin-top: 45px; margin-bottom: 5px; }" +
				".clear { clear: both; }" +
				".title{ margin: 50px 0px 50px 0px; }" +
				"</style>" +
				"</head>" +
				"<body><div class=\"container\">" +
				"<h3 class=\"title\">비밀번호 재설정 링크를 보내드립니다.</h3>" +
				"<div>" +
				"<p>아래의 비밀번호 재설정 버튼을 클릭해 새로운 비밀번호를 설정할 수 있습니다.</p>" +
				"<p>본인이 요청한 것이 아닌경우, 본 메일은 무시하셔도 됩니다.</p>" +
				"</div>" +
				"<div><a href=\"" + link + "\"  class=\"button btnPush realButton\">비밀번호 변경하기</a></div>" +
				"<div class=\"clear\"></div>" +
				"</div></body></html>";
	}

}