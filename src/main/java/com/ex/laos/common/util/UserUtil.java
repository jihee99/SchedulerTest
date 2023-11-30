package com.ex.laos.common.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ex.laos.common.dto.MemberFormDto;
import com.ex.laos.common.service.CustomUserDetails;

public class UserUtil {

	private UserUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static MemberFormDto getUserDto() {
		try {
			return ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemberFormDto();
		} catch(NullPointerException | ClassCastException e) {
			return null;
		}
	}

	/**
	 * 세션 사용자 권한 가져오는 메서드
	 * @return 사용자 권한
	 */
	public static String getSevcAuthrt() {
		MemberFormDto userDto = UserUtil.getUserDto();
		if(userDto != null) {
			return userDto.getSevcAuthrtId();
		}else{
			return "";
		}
	}

	public static boolean isAllAccess() {
		String role = UserUtil.getSevcAuthrt();
		return role.equals("");
	}


	// public static boolean isUser() {
	// 	String role = UserUtil.getSevcAuthrt();
	// 	return role.equals("USER");
	// }


}
