package com.bookstore.onlinebookstore.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
			LocalDateTime timestamp,
			int status,
			String error,
			String message
		) {

}
