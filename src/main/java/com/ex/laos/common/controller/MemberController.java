package com.ex.laos.common.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ex.laos.common.dto.MailTokenDto;
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
	 *	put으로 바꿔도 되는가
	 * */
	@PostMapping("/update/pwd/process2")
	public Map<String, Object> updatePassword(
		@RequestParam String username, HttpSession session
	){

		Map<String, Object> response = new HashMap<>();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = null;
		if (authentication != null && authentication.isAuthenticated()) {
			userId = authentication.getName();
		}

		if (userId.equals(username)) {
			try{
				mailService.sendEmailForPasswordReset(username);

				response.put("status", "success");
				response.put("message", "메일이 전송되었습니다.");
			} catch (MessagingException e){
				response.put("status", "error");
				response.put("message", "메일 전송에 실패했습니다. 관리자에게 문의해주세요.");
				// response.put("message", "메일 전송에 실패했습니다. 로그인 정보를 확인해주세요.");
			}

		} else {
			response.put("status", "error");
			response.put("message", "로그인 정보를 확인해주세요.");
		}

		return response;
	}


	@GetMapping("/password/reset")
	public ModelAndView viewConfirmEmail(@RequestParam("token") String token, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();

		MailTokenDto mailTokenDto = mailService.isTokenValid(token);

		if(mailTokenDto != null){
			// 비밀번호 변경 화면 반환
			modelAndView.setViewName("updatePwdLink");
			session.setAttribute("mailToken", mailTokenDto);
		}else{
			modelAndView.setViewName("invalidPage");
		}
		return modelAndView;
	}
	
	
	@PostMapping("/password/update-process")
	public Map<String, Object> updatePassword(
		@RequestParam String rawPwd, @RequestParam String newPwd, @RequestParam String newPwdChk, HttpSession session
	){
		Map<String, Object> response = new HashMap<>();

		// 세션에서 데이터 가져오기
		MailTokenDto mailToken = (MailTokenDto) session.getAttribute("mailToken");
		System.out.println(mailToken);

		if (mailToken != null) {

			// 1. 현재 비밀번호 확인
			if(customUserDetailsService.isPasswordValid(mailToken.getMbrId(), rawPwd)){

				// 2. 현재 비밀번호와 변경하는 비밀번호가 동일한지 확인
				if(!rawPwd.equals(newPwd)){

				}else{
					response.put("status", "error");
					response.put("message", "새로운 비밀번호가 현재 비밀번호와 일치합니다.");
				}
			}else{
				// 현재 비밀번호가 일치하지 않는 다 반환
				response.put("status", "error");
				response.put("message", "현재 비밀번호가 일치하지 않습니다.");
			}
		}else{
			// 세션이 만료되었습니다 반환
			response.put("status", "error");
			response.put("message", "세션이 만료되었습니다.");
		}

		return response;
	}

}

