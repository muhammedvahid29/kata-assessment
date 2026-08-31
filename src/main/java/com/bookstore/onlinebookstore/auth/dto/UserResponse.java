package com.bookstore.onlinebookstore.auth.dto;

import com.bookstore.onlinebookstore.auth.enums.Role;

public record UserResponse(
			Long id,
			String email,
			Role role
		) {

}
