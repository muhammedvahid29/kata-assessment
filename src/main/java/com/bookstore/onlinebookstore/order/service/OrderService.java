package com.bookstore.onlinebookstore.order.service;

import java.util.List;

import com.bookstore.onlinebookstore.order.dto.OrderResponse;

public interface OrderService {
	OrderResponse placeOrder(Long userId);
	
	OrderResponse getOrderById(
				Long userId,
				Long orderId
			);
	
	List<OrderResponse> getMyOrders(Long userId);
}
