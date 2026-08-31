package com.bookstore.onlinebookstore.cart.service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.onlinebookstore.book.dto.BookResponse;
import com.bookstore.onlinebookstore.book.service.BookService;
import com.bookstore.onlinebookstore.cart.dto.AddCartItemRequest;
import com.bookstore.onlinebookstore.cart.dto.CartItemResponse;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.dto.UpdateCartItemRequest;
import com.bookstore.onlinebookstore.cart.entity.Cart;
import com.bookstore.onlinebookstore.cart.entity.CartItem;
import com.bookstore.onlinebookstore.cart.repository.CartRepository;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class CartServiceImpl implements CartService {
	
	BookService bookService;
	CartRepository cartRepository;
	
	@Override
	@Transactional(readOnly = true)
	public CartResponse getCart(Long userid) {
		
		var cart = cartRepository
				.findByUserId(userid)
				.orElseGet(() ->
							Cart.builder()
								.userId(userid)
								.build()
						);
		
		return toResponse(cart);
	}
	
	@Override
	public CartResponse addItem(Long userId, AddCartItemRequest request) {
		
		bookService.getBookById(request.bookId());
		var cart = getOrCreateCart(userId);
		
		var existingItem = cart.getItems()
				.stream()
				.filter(item ->
						item.getBookId().equals(request.bookId())
						)
				.findFirst();
		
		if(existingItem.isPresent()) {
			var item = existingItem.get();
			item.setQuantity(
					item.getQuantity()
							+ request.quantity()
			);
		} else {
			var item = CartItem.builder()
						.cart(cart)
						.bookId(request.bookId())
						.quantity(request.quantity())
						.build();
			cart.getItems().add(item);
		}
		
		var savedCart = cartRepository.save(cart);
		
		return toResponse(savedCart);
	}

	@Override
	public CartResponse updateItem(Long userId, Long bookId, UpdateCartItemRequest request) {
		var cart = getCartEntity(userId);
		
		var item = cart.getItems()
					   .stream()
					   .filter(i ->
					   		i.getBookId().equals(bookId)
					   )
					   .findFirst()
					   .orElseThrow(() -> 
					   			new ResourceNotFoundException(
					   					"Book Not Found for bookId: "+bookId
							   ));
		item.setQuantity(request.quantity());
		return toResponse(cart);
	}

	@Override
	public void removeItem(Long userId, Long bookId) {
		var cart = getCartEntity(userId);
		
		boolean removed = cart.getItems()
							.removeIf(item ->
									item.getBookId().equals(bookId)
									);
		if(!removed) {
			throw new ResourceNotFoundException( 
						"book not found in cart for bookId: "+bookId
			);
		}
	}

	@Override
	public void clearCart(Long userId) {
		var cart = getCartEntity(userId);
		cart.getItems().clear();
	}
	
	
	private CartResponse toResponse(Cart cart) {
		
		var bookIds = cart.getItems()
						.stream()
						.map(CartItem::getBookId)
						.toList();
		
		var books = bookService.getBooksByIds(bookIds);
		
		var bookMap = books.stream()
						.collect(Collectors.toMap(
							BookResponse::id,
							book -> book
						));
		
		
		var items = cart.getItems()
					.stream()
					.map(item -> {
						BookResponse book = bookMap.get(item.getBookId());
						
						var subTotal = 
								book.price()
										.multiply(
												BigDecimal.valueOf(
														item.getQuantity()
										));
						return new CartItemResponse(
								book.id(),
								book.title(),
								book.price(),
								item.getQuantity(),
								subTotal
						);
					})
					.toList();
		var total = items.stream()
						.map(CartItemResponse::subTotal)
						.reduce(
							BigDecimal.ZERO,
							BigDecimal::add
						);
		
		return new CartResponse(
				cart.getId(),
				items,
				total
		);
	}
	
	private Cart getOrCreateCart(Long userId) {
		return cartRepository
					.findByUserId(userId)
					.orElseGet(() -> {
						var cart = Cart.builder()
										.userId(userId)
										.build();
						return cartRepository.save(cart);
					});
	}
	
	private Cart getCartEntity(Long userId) {
		return cartRepository.findByUserId(userId)
							 .orElseThrow(() -> 
							 		new ResourceNotFoundException(
							 			"Cart Not Found For user :" + userId
							 ));
	}

}
