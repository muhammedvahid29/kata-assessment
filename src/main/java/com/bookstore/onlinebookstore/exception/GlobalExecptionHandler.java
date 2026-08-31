package com.bookstore.onlinebookstore.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExecptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(
			ResourceNotFoundException ex){
		 var response = new ErrorResponse(
				 LocalDateTime.now().withNano(0),
				 404,
				 "NOT_FOUND",
				 ex.getMessage()
		);
		 
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(response);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidation(
			MethodArgumentNotValidException ex) {
		
		var erros = ex.getBindingResult()
					.getFieldErrors()
					.stream()
					.collect(Collectors.toMap(
							error -> error.getField(),
							error -> error.getDefaultMessage(),
							(first,second) ->  first
							));
		
		var response = new ValidationErrorResponse(
				LocalDateTime.now().withNano(0),
				400,
				"VALIDATION_FAILED",
				erros);
		
		return ResponseEntity
				.badRequest()
				.body(response);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex){
		var response = new ErrorResponse(
				LocalDateTime.now().withNano(0),
				400,
				"BAD_REQUEST",
				ex.getMessage()
		);
		
		return ResponseEntity
				.badRequest()
				.body(response);
	}
	
}
