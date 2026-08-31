package com.bookstore.onlinebookstore.auth.service;

import com.bookstore.onlinebookstore.auth.dto.RegisterRequest;
import com.bookstore.onlinebookstore.auth.dto.UserResponse;

public interface AuthService {
	UserResponse register(RegisterRequest request);
}
