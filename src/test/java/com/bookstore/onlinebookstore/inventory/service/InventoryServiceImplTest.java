package com.bookstore.onlinebookstore.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.onlinebookstore.exception.InsufficientStockException;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;
import com.bookstore.onlinebookstore.inventory.dto.InventoryRequest;
import com.bookstore.onlinebookstore.inventory.entity.Inventory;
import com.bookstore.onlinebookstore.inventory.repository.InventoryRepository;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private InventoryRequest inventoryRequest;

    @BeforeEach
    void setUp() {

        inventory = Inventory.builder()
                .id(1L)
                .bookId(10L)
                .quantity(20)
                .build();

        inventoryRequest = new InventoryRequest(
                10L,
                20
        );
    }

    @Test
    void createInventory_shouldCreateInventory() {

        when(inventoryRepository.existsByBookId(10L))
                .thenReturn(false);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(inventory);

        var response =
                inventoryService.createInventory(inventoryRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(10L, response.bookId());
        assertEquals(20, response.quantity());

        verify(inventoryRepository)
                .existsByBookId(10L);

        verify(inventoryRepository)
                .save(any(Inventory.class));
    }

    @Test
    void createInventory_shouldThrowExceptionWhenInventoryAlreadyExists() {

        when(inventoryRepository.existsByBookId(10L))
                .thenReturn(true);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.createInventory(
                        inventoryRequest
                )
        );

        assertEquals(
                "Inventory already exists for book: 10",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .existsByBookId(10L);
    }

    @Test
    void getInventoryByBookId_shouldReturnInventory() {

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.of(inventory));

        var response =
                inventoryService.getInventoryByBookId(10L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(10L, response.bookId());
        assertEquals(20, response.quantity());

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void getInventoryByBookId_shouldThrowExceptionWhenNotFound() {

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> inventoryService.getInventoryByBookId(10L)
        );

        assertEquals(
                "Inventory not found for book: 10",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void updateInventory_shouldUpdateQuantity() {

        var request = new InventoryRequest(
                10L,
                50
        );

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.of(inventory));

        var response =
                inventoryService.updateInventory(
                        10L,
                        request
                );

        assertNotNull(response);
        assertEquals(50, response.quantity());
        assertEquals(50, inventory.getQuantity());

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void updateInventory_shouldThrowExceptionWhenNotFound() {

        var request = new InventoryRequest(
                10L,
                50
        );

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> inventoryService.updateInventory(
                        10L,
                        request
                )
        );

        assertEquals(
                "Inventory not found for book: 10",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void increaseStock_shouldIncreaseQuantity() {

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.of(inventory));

        inventoryService.increaseStock(10L, 5);

        assertEquals(25, inventory.getQuantity());

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void increaseStock_shouldThrowExceptionForInvalidQuantity() {

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.increaseStock(10L, 0)
        );

        assertEquals(
                "Quantity must be greater than zero",
                exception.getMessage()
        );

        verify(inventoryRepository,
                org.mockito.Mockito.never())
                .findByBookId(10L);
    }

    @Test
    void increaseStock_shouldThrowExceptionWhenInventoryNotFound() {

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> inventoryService.increaseStock(10L, 5)
        );

        assertEquals(
                "Inventory not found for book: 10",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void decreaseStock_shouldDecreaseQuantity() {

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.of(inventory));

        inventoryService.decreaseStock(10L, 5);

        assertEquals(15, inventory.getQuantity());

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void decreaseStock_shouldThrowExceptionForInvalidQuantity() {

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.decreaseStock(10L, 0)
        );

        assertEquals(
                "Quantity must be greater than zero",
                exception.getMessage()
        );

        verify(inventoryRepository,
                org.mockito.Mockito.never())
                .findByBookId(10L);
    }

    @Test
    void decreaseStock_shouldThrowExceptionWhenInventoryNotFound() {

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> inventoryService.decreaseStock(10L, 5)
        );

        assertEquals(
                "Inventory not found for book: 10",
                exception.getMessage()
        );

        verify(inventoryRepository)
                .findByBookId(10L);
    }

    @Test
    void decreaseStock_shouldThrowExceptionWhenStockIsInsufficient() {

        inventory.setQuantity(3);

        when(inventoryRepository.findByBookId(10L))
                .thenReturn(Optional.of(inventory));

        var exception = assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.decreaseStock(10L, 5)
        );

        assertEquals(
                "Insufficient stock for book: 10",
                exception.getMessage()
        );

        assertEquals(3, inventory.getQuantity());

        verify(inventoryRepository)
                .findByBookId(10L);
    }
}