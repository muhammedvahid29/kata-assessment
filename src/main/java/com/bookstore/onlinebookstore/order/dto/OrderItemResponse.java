package com.bookstore.onlinebookstore.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
			Long bookId,
			String bookTitle,
			BigDecimal price,
			Integer quantity,
			BigDecimal subTotal
		) {

}
