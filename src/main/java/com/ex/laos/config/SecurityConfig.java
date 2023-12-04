package com.ex.laos.config;

import javax.sql.DataSource;

import com.ex.laos.common.service.CustomUserDetailsService;
import com.ex.laos.common.service.LoginSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final DataSource dataSource;

	// public SecurityConfig(CustomUserDetailsService userDetailsService) {
	// 	this.userDetailsService = userDetailsService;
	// }

	// 패스워드 인코더로 사용할 빈 등록
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// SecurityFilterChain을 설정하는 빈 등록
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// .csrf().disable()
			.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Use a cookie to store the CSRF token
				.and()
			.cors().disable() // CORS 보안 설정 비활성화
				// .cors().and()
				// .headers().frameOptions().sameOrigin().and()
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
					.antMatchers("/favicon.ico", "/ko", "/lecture", "/css/**", "/js/**", "/join", "/join/member").permitAll() // 특정 경로는 인증 없이 접근 허용
					.anyRequest().authenticated() // 그 외의 요청은 인증 필요
			)
				// 로그인 설정
			.formLogin(formLogin ->
				formLogin
					.loginPage("/login") // 로그인 페이지 설정
					.loginProcessingUrl("/login-process") // 실제 로그인 처리 URL
					.usernameParameter("username") // 사용자 이름 파라미터명
					.passwordParameter("password") // 비밀번호 파라미터명
//                                .defaultSuccessUrl(test(), true) // 로그인 성공 후 리다이렉트할 URL
					.permitAll() // 로그인 페이지는 인증 없이 접근 허용
					.successHandler(new LoginSuccessHandler())
					.failureHandler((request, response, exception) -> {
						System.out.println("exception: " + exception.getMessage());
						response.sendRedirect("/login");
					})
			)
			// 로그아웃 설정
			.logout(logout ->
				logout
					.logoutSuccessUrl("/login") // 로그아웃 후 리다이렉트할 URL
			);

		// 커스텀 필터 추가
		// http.addFilterAfter(new CustomHeaderFilter(), SecurityContextPersistenceFilter.class);

		return http.build();// 설정된 HttpSecurity 객체 반환
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

		return daoAuthenticationProvider;
	}

	// @Bean
	// public PersistentTokenRepository rememberMeTokenRepository() {
	// 	JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
	// 	jdbcTokenRepository.setDataSource(dataSource);
	// 	return jdbcTokenRepository;
	// }

	// 커스텀 필터 클래스 정의
// 	private class CustomHeaderFilter extends OncePerRequestFilter {
// 		@Override
// 		protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException,IOException,IOException {
// //            if (SecurityUtil.isLoggedIn()) {
// //                response.setHeader("Custom-Header", "User is logged in");
// //            } else {
// //                response.setHeader("Custom-Header", "User is not logged in");
// //            }
// //            filterChain.doFilter(request, response);
//
// 			if (SecurityUtil.isLoggedIn()) {
// 				request.setAttribute("isLoggedIn",true);
// 			} else {
// 				request.setAttribute("isLoggedIn",false);
// 			}
// 			filterChain.doFilter(request,response);
// 		}
// 	}
}
