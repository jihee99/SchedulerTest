package com.ex.laos.common.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.common.dto.MemberDto;
import com.ex.laos.common.service.CustomUserDetailsService;
import com.ex.laos.common.service.MailService;

@RestController
public class MemberController {

	private final CustomUserDetailsService customUserDetailsService;

	private final MailService mailService;

	public MemberController(CustomUserDetailsService customUserDetailsService, MailService mailService){
		this.customUserDetailsService = customUserDetailsService;
		this.mailService = mailService;
	}

	@PostMapping("/join/member")
	public void save(@ModelAttribute MemberDto member, HttpServletResponse response) throws IOException {
		customUserDetailsService.insertMember(member);
		response.sendRedirect("/login");
	}


	// type 1
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


	// type 2
	@PostMapping("/update/pwd/process2")
	public Map<String, Object> updatePassword(
		@RequestParam String username,
		HttpSession session
	) {
		Map<String, Object> response = new HashMap<>();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		if (authentication != null && authentication.isAuthenticated()) {
			userId = authentication.getName();
		}

		if (userId.equals(username)) {
			mailService.sendEmailForPasswordReset();

			Map<String, Object> authMap = new HashMap<>();
			long createTime = System.currentTimeMillis();
			long endTime = createTime + ( 300 * 1000 );

			authMap.put("createTime", createTime);
			authMap.put("endTime", endTime);

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

}

