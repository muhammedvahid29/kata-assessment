package com.bookstore.onlinebookstore.order.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "order_id",
			nullable = false
	)
	Order order;
	
	@Column(name = "book_id",nullable = false)
	Long bookId;
	
	@Column(name = "book_title", nullable = false)
	String bookTitle;
	
	@Column(nullable = false,precision = 10,scale = 2)
	BigDecimal price;
	
	@Column(nullable = false)
	Integer quantity;
	
	@Column(nullable = false)
	BigDecimal subtotal;
}
