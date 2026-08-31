package com.bookstore.onlinebookstore.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.onlinebookstore.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	Optional<CartItem> findByCartIdAndBookId(
			Long cartId,
			Long bookId
	);
}
