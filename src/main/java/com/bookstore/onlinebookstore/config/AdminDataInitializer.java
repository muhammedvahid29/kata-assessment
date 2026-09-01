package com.bookstore.onlinebookstore.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bookstore.onlinebookstore.auth.entity.User;
import com.bookstore.onlinebookstore.auth.enums.Role;
import com.bookstore.onlinebookstore.auth.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Component
public class AdminDataInitializer implements CommandLineRunner {
	
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	
	
	@Override
	public void run(String... args) throws Exception {
		boolean adminExists =
					userRepository.existsByRole(Role.ADMIN);
		
		if(!adminExists) {
			var admin = User.builder()
						.email("admin@onlinebookstore.com")
						.password(passwordEncoder.encode("admin123"))
						.role(Role.ADMIN)
						.build();
			
			userRepository.save(admin);
		}
	}

}
