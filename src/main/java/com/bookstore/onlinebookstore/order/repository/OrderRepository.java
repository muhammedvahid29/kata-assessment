package com.bookstore.onlinebookstore.order.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bookstore.onlinebookstore.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
