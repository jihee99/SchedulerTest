package com.ex.laos.config;

import com.ex.laos.common.service.LoginSuccessHandler;
import com.ex.laos.common.util.SecurityUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


	// SecurityFilterChain을 설정하는 빈
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// http
		// 	// 요청 권한 설정
		// 	.authorizeHttpRequests(request -> request
		// 		.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // FORWARD로 전달되는 요청은 인증 없이 허용
		// 		.requestMatchers("/ko/**","/lecture","/css/**","/js/**").permitAll() // 특정 URL 패턴은 인증 없이 허용
		// 		.anyRequest().authenticated() // 그 외의 요청은 인증이 필요함
		// 	)
		//
		// 	// 로그인 설정
		// 	.formLogin(login -> login
		// 		.loginPage("/ko") // 커스텀 로그인 페이지 URL 설정
		// 		.loginProcessingUrl("/login-process") // 로그인 폼 submit URL 설정
		// 		//                        .loginProcessingUrl("/login_proc") // 로그인 폼 submit URL 설정
		// 		.usernameParameter("userEmail") // 아이디 파라미터명 설정
		// 		.passwordParameter("userPassword") // 비밀번호 파라미터명 설정
		// 		.defaultSuccessUrl("/ko",true) // 로그인 성공 후 이동할 페이지 설정
		// 		.permitAll() // 로그인 페이지는 인증 없이 접근 허용
		//
		// 		.successHandler(new AuthenticationSuccessHandler() {
		// 			@Override
		// 			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		// 				Authentication authentication) throws IOException, ServletException {
		// 				System.out.println("authentication : "+authentication.getName() );
		// 				response.sendRedirect("/ko");
		// 			}
		// 		})
		//
		// 		.failureHandler(new AuthenticationFailureHandler() {
		// 			@Override
		// 			public void onAuthenticationFailure(HttpServletRequest request,HttpServletResponse response,AuthenticationException exception) throws IOException,ServletException {
		// 				System.out.println("exception : "+ exception.getMessage());
		// 				response.sendRedirect("/ko");
		// 			}
		// 		})
		// 	)
		//
		// 	// 로그아웃 설정
		// 	.logout(logout -> logout
		// 		.logoutSuccessUrl("/ko")
		// 	) // 로그아웃 설정은 기본 설정을 사용
		//
		// 	.addFilterAfter(new CustomHeaderFilter(), SecurityContextPersistenceFilter.class);// 커스텀 필터 추가

		http
				.cors().disable() // CORS 보안 설정 비활성화
				.authorizeRequests(authorizeRequests ->
					authorizeRequests
						.antMatchers("/favicon.ico", "/ko", "/lecture", "/css/**", "/js/**").permitAll() // 특정 경로는 인증 없이 접근 허용
						.anyRequest().authenticated() // 그 외의 요청은 인증 필요
				)
				.formLogin(formLogin ->
					formLogin
						.loginPage("/home") // 로그인 페이지 설정
						.loginProcessingUrl("/login-process") // 실제 로그인 처리 URL
						.usernameParameter("mbrId") // 사용자 이름 파라미터명
						.passwordParameter("pswd") // 비밀번호 파라미터명
//                                .defaultSuccessUrl(test(), true) // 로그인 성공 후 리다이렉트할 URL
						.permitAll() // 로그인 페이지는 인증 없이 접근 허용
						.successHandler(new LoginSuccessHandler())
						.failureHandler((request, response, exception) -> {
							System.out.println("exception: " + exception.getMessage());
							response.sendRedirect("/ko");
						})
				)

				.logout(logout ->
					logout
						.logoutSuccessUrl("/ko") // 로그아웃 후 리다이렉트할 URL
				);

		// 커스텀 필터 추가
		http.addFilterAfter(new CustomHeaderFilter(), SecurityContextPersistenceFilter.class);

		return http.build();// 설정된 HttpSecurity 객체 반환
	}


	// 커스텀 필터 클래스 정의
	private class CustomHeaderFilter extends OncePerRequestFilter {
		@Override
		protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException,IOException,IOException {
//            if (SecurityUtil.isLoggedIn()) {
//                response.setHeader("Custom-Header", "User is logged in");
//            } else {
//                response.setHeader("Custom-Header", "User is not logged in");
//            }
//            filterChain.doFilter(request, response);

			if (SecurityUtil.isLoggedIn()) {
				request.setAttribute("isLoggedIn",true);
			} else {
				request.setAttribute("isLoggedIn",false);
			}
			filterChain.doFilter(request,response);
		}
	}
}
