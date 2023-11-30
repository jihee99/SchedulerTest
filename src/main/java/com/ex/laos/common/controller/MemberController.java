package com.ex.laos.common.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.common.dto.MemberFormDto;
import com.ex.laos.common.service.CustomUserDetailsService;

@RestController
public class MemberController {

	private final CustomUserDetailsService customUserDetailsService;


	public MemberController(CustomUserDetailsService customUserDetailsService){
		this.customUserDetailsService = customUserDetailsService;
	}

	@PostMapping("/api/member")
	public void save(@RequestBody MemberFormDto member) {
		customUserDetailsService.save(member);
	}

}
