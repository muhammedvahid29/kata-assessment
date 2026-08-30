package com.bookstore.onlinebookstore.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRequest(
		    @NotNull(message = "BookId is required")
			Long bookId,
			
			@NotNull(message = "Quantity is required")
		    @Min(value = 0,message = "Quantity cannot be negative")
			Integer quantity
		) {

}
