package com.bookstore.onlinebookstore.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
			Long cartId,
			List<CartItemResponse> items,
			BigDecimal totalAmount
		) {

}
