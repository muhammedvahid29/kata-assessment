package com.bookstore.onlinebookstore.book.dto;

import java.math.BigDecimal;

public record BookResponse(
			Long id,
			String title,
			String author,
			BigDecimal price
		) {
}
