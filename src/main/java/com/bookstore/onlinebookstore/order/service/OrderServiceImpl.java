package com.bookstore.onlinebookstore.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.onlinebookstore.cart.dto.CartItemResponse;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.service.CartService;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;
import com.bookstore.onlinebookstore.inventory.service.InventoryService;
import com.bookstore.onlinebookstore.order.dto.OrderItemResponse;
import com.bookstore.onlinebookstore.order.dto.OrderResponse;
import com.bookstore.onlinebookstore.order.entity.Order;
import com.bookstore.onlinebookstore.order.entity.OrderItem;
import com.bookstore.onlinebookstore.order.enums.OrderStatus;
import com.bookstore.onlinebookstore.order.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	OrderRepository orderRepository;
	CartService cartService;
	InventoryService inventoryService;
	
	
	@Override
	public OrderResponse placeOrder(Long userId) {
		CartResponse cart = cartService.getCart(userId);
		
		if(cart.items().isEmpty()) {
			throw new IllegalArgumentException(
						"Cannot Place order with a empty cart"
			);
		}
		
		var order = Order.builder()
						.userId(userId)
						.status(OrderStatus.PLACED)
						.createdAt(LocalDateTime.now())
						.totalAmount(cart.totalAmount())
						.build();
		
		for(CartItemResponse cartItem : cart.items()) {
			inventoryService.decreaseStock(
					cartItem.bookId(),
					cartItem.quantity()
			);
			
			var orderItem = OrderItem.builder()
					.order(order)
					.bookId(cartItem.bookId())
					.bookTitle(cartItem.title())
					.price(cartItem.price())
					.quantity(cartItem.quantity())
					.subtotal(cartItem.subTotal())
					.build();
			order.getItems().add(orderItem);
		}
		
		var savedOrder = orderRepository.save(order);
		
		cartService.clearCart(userId);
		
		return toResponse(savedOrder);
	}

	@Override
	public OrderResponse getOrderById(Long userId, Long orderId) {
		
		var order = orderRepository.findById(orderId)
						.orElseThrow(() -> 
							new ResourceNotFoundException(
									"Order not found with id: "+orderId
						));
		
		if(!order.getUserId().equals(userId)) {
			throw new ResourceNotFoundException(
				"Order not found with id: "+orderId	
			);
		}
		
		return toResponse(order);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getMyOrders(Long userId) {
		return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
						.stream()
						.map(this::toResponse)
						.toList();
	}
	
	private OrderResponse toResponse(Order order) {
		
		var items = order.getItems().stream()
									.map(item -> new OrderItemResponse(
											item.getBookId(),
											item.getBookTitle(),
											item.getPrice(),
											item.getQuantity(),
											item.getSubtotal()
									))
									.toList();
		return new OrderResponse(
				order.getId(),
				order.getUserId(),
				order.getStatus(),
				order.getTotalAmount(),
				order.getCreatedAt(),
				items
		);
	}

}
