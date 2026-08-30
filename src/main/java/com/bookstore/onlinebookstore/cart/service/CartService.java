package com.bookstore.onlinebookstore.cart.service;

import com.bookstore.onlinebookstore.cart.dto.AddCartItemRequest;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.dto.UpdateCartItemRequest;

public interface CartService {

	CartResponse getCart(Long userid);
	
	CartResponse addItem(Long userId,AddCartItemRequest request);
	
	CartResponse updateItem(Long userId,Long bookId,UpdateCartItemRequest request);
	
	void removeItem(Long userId,Long bookId);
	
	void clearCart(Long userId);
}
