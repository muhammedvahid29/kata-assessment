package com.bookstore.onlinebookstore.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRequest(
		    @NotNull(message = "BookId is required")
			Long bookId,
			
			@NotNull(message = "Quantity is required")
		    @Min(value = 1,message = "At least one quantity is required")
			Integer quantity
		) {

}
