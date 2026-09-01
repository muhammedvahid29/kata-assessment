package com.bookstore.onlinebookstore.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.onlinebookstore.inventory.dto.InventoryRequest;
import com.bookstore.onlinebookstore.inventory.dto.InventoryResponse;
import com.bookstore.onlinebookstore.inventory.service.InventoryService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/inventory")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class InventoryController {
	
	InventoryService inventoryService;
	
	@PostMapping
	public ResponseEntity<InventoryResponse> createInventory(
				@Valid @RequestBody InventoryRequest inventoryRequest
			){
		
		var response = inventoryService.createInventory(inventoryRequest);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@GetMapping("/{bookId}")
	public ResponseEntity<InventoryResponse> getInventory(
				@PathVariable Long bookId){
		return ResponseEntity.ok(
					inventoryService.getInventoryByBookId(bookId)
				);
	}
	
	@PutMapping
	public ResponseEntity<InventoryResponse> updateInventory(
				@Valid @RequestBody InventoryRequest inventoryRequest) {
		return ResponseEntity.ok(
					inventoryService.updateInventory(
							inventoryRequest.bookId(), 
							inventoryRequest
					)
				);
	}
}
