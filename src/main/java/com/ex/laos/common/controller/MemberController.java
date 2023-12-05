package com.ex.laos.common.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.common.dto.MemberDto;
import com.ex.laos.common.service.CustomUserDetailsService;
import com.ex.laos.common.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final CustomUserDetailsService customUserDetailsService;

	private final JavaMailSender javaMailSender;
	private final MailService mailService;


	@PostMapping("/join/member")
	public void save(@ModelAttribute MemberDto member, HttpServletResponse response) throws IOException {
		customUserDetailsService.insertMember(member);
		response.sendRedirect("/login");
	}


	/**
	 *  화면에서 비밀번호 입력받고 업데이트 하는 메서드
	 * */
	@PostMapping("/update/pwd/process")
	public Map<String, Object> updatePassword(
		@RequestParam String currentPwd, @RequestParam String newPwd1, @RequestParam String newPwd2
	){
		Map<String, Object> response = new HashMap<>();
		/**
		 * 1. userId 가 비었는지 확인
		 * 2. currentPwd가 일치하는지 확인
		 * */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		if (authentication != null && authentication.isAuthenticated()) {
			userId = authentication.getName();
		}

		if (userId == null || userId.isEmpty()) {
			response.put("status", "success");
			response.put("message", "로그인 정보를 확인해주세요.");
		}else{
			if(customUserDetailsService.isPasswordValid(userId, currentPwd)){

				customUserDetailsService.updatePassword(userId, newPwd1);
				response.put("status", "success");
				response.put("message", "비밀번호가 성공적으로 변경되었습니다.");

			}else{
				response.put("status", "error");
				response.put("message", "현재 비밀번호가 일치하지 않습니다.");
			}
		}

		return response;
	}


	/**
	 *  입력한 이메일로 링크 / form 전송해 비밀번호 변경하는 메서드
	 *
	 * */
	@PostMapping("/update/pwd/process2")
	public Map<String, Object> updatePassword(
		@RequestParam String username,
		HttpSession session
	) throws Exception {
		Map<String, Object> response = new HashMap<>();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		if (authentication != null && authentication.isAuthenticated()) {
			userId = authentication.getName();
		}

		if (userId.equals(username)) {
			mailService.sendEmailForPasswordReset(username);

			// session.setMaxInactiveInterval(300);
			// session.setAttribute("auth", authMap);

			response.put("status", "success");
			response.put("message", "사용자 이름 일치함!!");

		} else {
			response.put("status", "error");
			response.put("message", "로그인 정보를 확인해주세요.");
		}

		return response;
	}

	// public MimeMessage createMessage(String to)
	// 	throws UnsupportedEncodingException, MessagingException {
	// 	MimeMessage message = javaMailSender.createMimeMessage();
	//
	// 	message.addRecipients(MimeMessage.RecipientType.TO, to);
	// 	message.setSubject("sbb 임시 비밀번호");
	//
	// 	String msgg = "";
	// 	msgg += "<div style='margin:100px;'>";
	// 	msgg +=
	// 		"<div align='center' style='border:1px solid black; font-family:verdana';>";
	// 	msgg += "<h3 style='color:blue;'>임시 비밀번호입니다.</h3>";
	// 	msgg += "<div style='font-size:130%'>";
	// 	msgg += "CODE : <strong>";
	// 	msgg += ePw + "</strong><div><br/> ";
	// 	msgg += "</div>";
	// 	message.setText(msgg, "utf-8", "html");
	// 	message.setFrom(new InternetAddress("jea5158@gmail.com", "sbb_Admin"));
	//
	// 	return message;
	// }

}

