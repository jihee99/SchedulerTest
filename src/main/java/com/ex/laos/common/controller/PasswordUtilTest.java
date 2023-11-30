package com.ex.laos.common.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtilTest {
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	public static boolean matches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public static void main(String[] args){
		String rawPassword = "testpassword";

		// 암호화된 비밀번호 생성
		String encodedPassword = encodePassword(rawPassword);
		System.out.println("Encoded Password: " + encodedPassword);

		// 암호화된 비밀번호 검증
		boolean matches = matches(rawPassword, encodedPassword);
		System.out.println("Password Matches: " + matches);



	}
}
