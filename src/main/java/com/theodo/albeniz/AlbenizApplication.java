package com.theodo.albeniz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class AlbenizApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlbenizApplication.class, args);
	}

}
