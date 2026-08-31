package com.bookstore.onlinebookstore.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.bookstore.onlinebookstore.order.enums.OrderStatus;

public record OrderResponse(
			Long orderId,
			Long userId,
			OrderStatus status,
			BigDecimal totalAmount,
			LocalDateTime createdAt,
			List<OrderItemResponse> items
		) {

}
