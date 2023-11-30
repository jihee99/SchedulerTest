package com.ex.laos.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import com.ex.laos.common.dto.MemberDto;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final MemberDto userDto;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userDto.getSevcAuthrtId()));
	}

	@Override
	public String getPassword() {
		return userDto.getPswd();
	}

	@Override
	public String getUsername() {
		return userDto.getMbrId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public MemberDto getMemberFormDto() {
		return this.userDto;
	}


}
