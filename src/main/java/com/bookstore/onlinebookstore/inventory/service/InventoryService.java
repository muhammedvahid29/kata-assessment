package com.bookstore.onlinebookstore.inventory.service;

import com.bookstore.onlinebookstore.inventory.dto.InventoryRequest;
import com.bookstore.onlinebookstore.inventory.dto.InventoryResponse;

public interface InventoryService {
	InventoryResponse createInventory(InventoryRequest inventoryRequest);
	InventoryResponse getInventoryByBookId(Long id);
	InventoryResponse updateInventory(Long bookId,InventoryRequest inventoryRequest);
	void increaseStock(Long bookId,int quantity);
	void decreaseStock(Long bookId,int quantity);
}
