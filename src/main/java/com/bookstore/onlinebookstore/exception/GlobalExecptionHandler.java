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
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex){
		var response = new ErrorResponse(
				LocalDateTime.now().withNano(0),
				HttpStatus.UNPROCESSABLE_CONTENT.value(),
				"INSUFFICIENT_STOCK",
				ex.getMessage()
		);
		
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_CONTENT)
				.body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalError(Exception ex){
		var response = new ErrorResponse(
					LocalDateTime.now().withNano(0),
					HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR",
					"An unexpected error occurred"
		);
		
		return ResponseEntity
				.internalServerError()
				.body(response);
	}
	
}
