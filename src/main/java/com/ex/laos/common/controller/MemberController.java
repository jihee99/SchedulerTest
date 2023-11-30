package com.ex.laos.common.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ex.laos.common.dto.MemberDto;
import com.ex.laos.common.service.CustomUserDetailsService;

@RestController
public class MemberController {

	private final CustomUserDetailsService customUserDetailsService;


	public MemberController(CustomUserDetailsService customUserDetailsService){
		this.customUserDetailsService = customUserDetailsService;
	}

	@PostMapping("/join/member")
	public void save(@ModelAttribute MemberDto member) {
		customUserDetailsService.insertMember(member);
	}



}
