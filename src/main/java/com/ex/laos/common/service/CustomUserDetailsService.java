package com.ex.laos.common.service;

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


	private UserDetails toUserDetails(MemberDto member) {
		return User.builder()
			.username(member.getMbrId())
			.password(member.getPswd())
			.authorities(new SimpleGrantedAuthority(member.getSevcAuthrtId()))
			.build();
	}

}
