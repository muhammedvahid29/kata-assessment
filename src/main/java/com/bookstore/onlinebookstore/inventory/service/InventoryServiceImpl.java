package com.bookstore.onlinebookstore.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.onlinebookstore.exception.InsufficientStockException;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;
import com.bookstore.onlinebookstore.inventory.dto.InventoryRequest;
import com.bookstore.onlinebookstore.inventory.dto.InventoryResponse;
import com.bookstore.onlinebookstore.inventory.entity.Inventory;
import com.bookstore.onlinebookstore.inventory.repository.InventoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    
	InventoryRepository inventoryRepository;
	
	@Override
	public InventoryResponse createInventory(InventoryRequest inventoryRequest) {
		
		if(inventoryRepository.existsByBookId(inventoryRequest.bookId())) {
			throw new IllegalArgumentException(
						"Inventory already exists for book: " + inventoryRequest.bookId()
					);
		}
		
		var inventory = Inventory.builder()
							.bookId(inventoryRequest.bookId())
							.quantity(inventoryRequest.quantity())
							.build();
		
		var savedInventory = inventoryRepository.save(inventory);
		return toResponse(savedInventory);
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponse getInventoryByBookId(Long bookId) {
		var inventory = findByBookId(bookId);
		return toResponse(inventory);
	}

	@Override
	public InventoryResponse updateInventory(Long bookId, InventoryRequest inventoryRequest) {
		var inventory = findByBookId(bookId);
		
		inventory.setQuantity(inventoryRequest.quantity());
		return toResponse(inventory);
	}

	@Override
	public void increaseStock(Long bookId, int quantity) {
		validateQuantity(quantity);
		
		var inventory = findByBookId(bookId);
		
		inventory.setQuantity(inventory.getQuantity() + quantity);
	}

	@Override
	public void decreaseStock(Long bookId, int quantity) {
		validateQuantity(quantity);
		
		var inventory = findByBookId(bookId);
		
		if(inventory.getQuantity() < quantity) {
			throw new InsufficientStockException(
					  "Insufficient stock for book: "+ bookId
					);
		}
		
		inventory.setQuantity(
					inventory.getQuantity() - quantity
				);
	}
	
	private InventoryResponse toResponse(Inventory inventory) {
		return new InventoryResponse(
				inventory.getId(),
				inventory.getBookId(),
				inventory.getQuantity()
			);
	}
	
	private Inventory findByBookId(Long bookId) {
		return inventoryRepository.findByBookId(bookId)
				.orElseThrow(() ->
					new ResourceNotFoundException(
								"Inventory not found for book: " + bookId
							)
				);
	}
	
	private void validateQuantity(int quantity) {
		if(quantity <= 0) {
			throw new IllegalArgumentException(
						"Quantity must be greater than zero"
					);
		}
	}

}
