package com.bookstore.onlinebookstore.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.onlinebookstore.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{
	Optional<Inventory> findByBookId(Long bookId);
	boolean existsByBookId(Long bookId);
}
