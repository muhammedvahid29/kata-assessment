package com.bookstore.onlinebookstore.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErrorResponse(
			@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
			LocalDateTime timestamp,
			int status,
			String error,
			String message
		) {

}
