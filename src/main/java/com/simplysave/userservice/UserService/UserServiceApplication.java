package com.simplysave.userservice.UserService;

import com.simplysave.userservice.UserService.models.Admin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
		System.out.print("Server running on port 8080...");

	}
}
