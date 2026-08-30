package com.bookstore.onlinebookstore.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.onlinebookstore.order.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(name = "user_id", nullable = false)
	Long userId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	OrderStatus status;
	
	@Column(name="total_amount",precision = 10,scale = 2,nullable = false)
	BigDecimal totalAmount;
	
	@Column(name="created_at",nullable = false)
	LocalDateTime createdAt;
	
	@OneToMany(
				mappedBy = "order",
				cascade = CascadeType.ALL,
				orphanRemoval = true
			)
	@Builder.Default
	List<OrderItem> items = new ArrayList<>();
}
