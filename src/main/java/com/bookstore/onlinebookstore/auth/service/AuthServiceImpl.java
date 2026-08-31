package com.bookstore.onlinebookstore.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.onlinebookstore.auth.dto.RegisterRequest;
import com.bookstore.onlinebookstore.auth.dto.UserResponse;
import com.bookstore.onlinebookstore.auth.entity.User;
import com.bookstore.onlinebookstore.auth.enums.Role;
import com.bookstore.onlinebookstore.auth.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class AuthServiceImpl implements AuthService {
	
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserResponse register(RegisterRequest request) {
		
		if(userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException(
						"Email already registered"
			);
		}
		var user = User.builder()
					.email(request.email())
					.password(
							passwordEncoder.encode(request.password())
					)
					.role(Role.CUSTOMER)
					.build();
		var savedUser = userRepository.save(user);
		return new UserResponse(
				savedUser.getId(),
				savedUser.getEmail(),
				savedUser.getRole()
		);
	}
	
}
