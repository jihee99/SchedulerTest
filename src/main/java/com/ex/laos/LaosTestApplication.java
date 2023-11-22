package com.ex.laos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LaosTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaosTestApplication.class, args);
	}

}
