package com.ex.laos.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nhncorp.lucy.security.xss.listener.SecurityUtils;

public class SecurityUtil extends SecurityUtils {
	public static boolean isLoggedIn() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated();
	}
}
