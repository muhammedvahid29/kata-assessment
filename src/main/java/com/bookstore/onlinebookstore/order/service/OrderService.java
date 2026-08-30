package com.bookstore.onlinebookstore.order.service;

import java.util.List;

import com.bookstore.onlinebookstore.order.dto.OrderResponse;
import com.bookstore.onlinebookstore.order.entity.OrderItem;

public interface OrderService {
	OrderResponse placeOrder(Long userId);
	
	OrderResponse getOrderById(
				Long userId,
				Long orderId
			);
	
	List<OrderItem> getMyOrders(Long userId);
}
