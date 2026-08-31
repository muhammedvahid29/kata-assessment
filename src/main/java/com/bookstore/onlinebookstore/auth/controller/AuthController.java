package com.bookstore.onlinebookstore.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.onlinebookstore.auth.dto.RegisterRequest;
import com.bookstore.onlinebookstore.auth.dto.UserResponse;
import com.bookstore.onlinebookstore.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
	AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(
			@Valid @RequestBody RegisterRequest request){
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(authService.register(request));
	}
}
