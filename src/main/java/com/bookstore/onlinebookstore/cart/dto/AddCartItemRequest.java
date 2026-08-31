package com.bookstore.onlinebookstore.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
		
			@NotNull(message="Book ID is required")
			Long bookId,
			
			@NotNull(message="Quantity is required")
			@Min(value = 1, message="Quantity must be atleast one")
			Integer quantity
		
		) {

}
