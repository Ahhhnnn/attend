package com.he.attend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.wf.jwtp.configuration.EnableJwtPermission;

@EnableJwtPermission
@ComponentScan("com.he.attend.*")
@SpringBootApplication
public class AttendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendApplication.class, args);
	}

}

