package com.ex.laos.common.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import com.ex.laos.common.dao.MemberDao;
import com.ex.laos.common.dto.MemberDto;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;

	public CustomUserDetailsService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void insertMember(MemberDto member) {
		MemberDto duplicateChk = memberDao.findByUserId(member.getMbrId());
		if(duplicateChk != null){
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
		member.setPswd(passwordEncoder.encode(member.getPswd()));
		memberDao.insertMember(member);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberDto member = memberDao.findByUserId(username);
		if (member == null) {
			throw new UsernameNotFoundException("User not found with username : " + username);
		}
		return new CustomUserDetails(member);
	}

	public boolean isPasswordValid(String username, String rawPassword) {
		MemberDto member = memberDao.findByUserId(username);
		if (member != null) {
			String encodedPassword = member.getPswd();
			return passwordEncoder.matches(rawPassword, encodedPassword);
		}
		return false;
	}

	@Transactional
	public void updatePassword(String username, String rawPassword){

		Map<String, String> userdata = new HashMap<>();
		userdata.put("username", username);
		userdata.put("pswdChgId", UUID.randomUUID().toString());

		// 1. 비밀번호 변경 이력 테이블에 insert
		memberDao.insertPasswordUpdateHistory(userdata);

		// 2. 사용자 정보 update
		String updatePwd = passwordEncoder.encode(rawPassword);
		System.out.println(updatePwd);
		memberDao.updatePasswordByUsername(username, updatePwd);
	}


	private UserDetails toUserDetails(MemberDto member) {
		return User.builder()
			.username(member.getMbrId())
			.password(member.getPswd())
			.authorities(new SimpleGrantedAuthority(member.getSevcAuthrtId()))
			.build();
	}

}
