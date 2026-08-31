package com.bookstore.onlinebookstore.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.onlinebookstore.order.dto.OrderResponse;
import com.bookstore.onlinebookstore.order.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class OrderController {
	OrderService orderService;
	
	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("X-USER-ID") Long userId){
		var response = orderService.placeOrder(userId);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@GetMapping
	public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestHeader("X-USER-ID") Long userId){
		
		return ResponseEntity.ok(
				orderService.getMyOrders(userId)
		);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrder(
			@RequestHeader("X-USER-ID") Long userId,
			@PathVariable Long orderId){
		
		return ResponseEntity.ok(
				orderService.getOrderById(
						userId,
						orderId)
		);
	}
}
