package com.ex.laos.common.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler {

	private final RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
	private final RequestCache requestCache = new HttpSessionRequestCache();
	private final DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		System.out.println("d이걸 탔을까요?? 아닐까요~!@!~");
		defaultRedirectStrategy.sendRedirect(request, response, "/home");
		try {
			super.onAuthenticationSuccess(request, response, authentication);
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}

	protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if(savedRequest!=null) {
			String targetUrl = savedRequest.getRedirectUrl();
			redirectStratgy.sendRedirect(request, response, targetUrl);
		} else {
			redirectStratgy.sendRedirect(request, response, "/ko");
		}

	}

}
