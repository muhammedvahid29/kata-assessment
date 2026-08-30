package com.bookstore.onlinebookstore.book.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
		    @NotBlank(message = "Title is required")
		    @Size(max = 200, message = "Title cannot exceed 200 characters")
			String title,
			
			@NotBlank(message = "Author is required")
		    @Size(max = 150,message = "Author cannot exceed 150 characters")
			String author,
			
			@NotNull(message = "Price is required")
		    @DecimalMin(value = "0.01",message = "Price must be greater than zero")
			BigDecimal price
		) {

}
