package com.bookstore.onlinebookstore.cart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.onlinebookstore.cart.dto.AddCartItemRequest;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.dto.UpdateCartItemRequest;
import com.bookstore.onlinebookstore.cart.entity.Cart;
import com.bookstore.onlinebookstore.cart.repository.CartRepository;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;
import com.bookstore.onlinebookstore.exception.UserNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class CartServiceImpl implements CartService {

	CartRepository cartRepository;
	
	@Override
	@Transactional(readOnly = true)
	public CartResponse getCart(Long userid) {
		
		Cart cart = cartRepository.findByUserId(userid).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
		
		return null;
	}
	
	@Override
	public CartResponse addItem(Long userId, AddCartItemRequest request) {

		return null;
	}

	@Override
	public CartResponse updateItem(Long userId, Long bookId, UpdateCartItemRequest request) {
		return null;
	}

	@Override
	public void removeItem(Long userId, Long bookId) {
		
	}

	@Override
	public void clearCart(Long userId) {
		
	}
	
	
	private CartResponse toresponse(Cart cart) {
		return new CartResponse(cart.getId(),cart.getItems(),cart.get);
	}

}
