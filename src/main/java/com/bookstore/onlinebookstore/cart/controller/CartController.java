package com.bookstore.onlinebookstore.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.onlinebookstore.cart.dto.AddCartItemRequest;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.dto.UpdateCartItemRequest;
import com.bookstore.onlinebookstore.cart.service.CartService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/carts")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class CartController {

	CartService cartService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
		return ResponseEntity
				.ok()
				.body(cartService.getCart(userId));
	}
	
	@PostMapping("/{userId}/items")
	public ResponseEntity<CartResponse> addItem(@PathVariable Long userId,@Valid @RequestBody AddCartItemRequest request) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(cartService.addItem(userId, request));
	}
	
	@PutMapping("/{userId}/items/{bookId}")
	public ResponseEntity<CartResponse> updateItem(@PathVariable Long userId,@PathVariable Long bookId,@Valid @RequestBody UpdateCartItemRequest request){
		return ResponseEntity
				.ok()
				.body(cartService.updateItem(userId, bookId, request));
	}
	
	@DeleteMapping("/{userId}/items/{bookId}")
	public ResponseEntity<Void> removeItem(@PathVariable Long userId,@PathVariable Long bookId) {
		
		cartService.removeItem(userId, bookId);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{userId}/items")
	public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
	
		cartService.clearCart(userId);
		
		return ResponseEntity.noContent().build();
	}
	
}
