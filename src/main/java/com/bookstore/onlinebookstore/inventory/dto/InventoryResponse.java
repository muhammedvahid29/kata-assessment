package com.bookstore.onlinebookstore.inventory.dto;

public record InventoryResponse(
			Long id,
			Long bookId,
			Integer quantity
		) {

}
