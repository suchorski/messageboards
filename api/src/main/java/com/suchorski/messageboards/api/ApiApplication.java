package com.suchorski.messageboards.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {

	public static final long VERSION = -1_004_001L;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
