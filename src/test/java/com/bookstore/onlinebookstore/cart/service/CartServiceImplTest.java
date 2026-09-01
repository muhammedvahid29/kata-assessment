package com.bookstore.onlinebookstore.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.onlinebookstore.book.dto.BookResponse;
import com.bookstore.onlinebookstore.book.service.BookService;
import com.bookstore.onlinebookstore.cart.dto.AddCartItemRequest;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.dto.UpdateCartItemRequest;
import com.bookstore.onlinebookstore.cart.entity.Cart;
import com.bookstore.onlinebookstore.cart.entity.CartItem;
import com.bookstore.onlinebookstore.cart.repository.CartRepository;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private BookService bookService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private CartItem cartItem;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {

        cart = Cart.builder()
                .id(1L)
                .userId(100L)
                .items(new ArrayList<>())
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .cart(cart)
                .bookId(10L)
                .quantity(2)
                .build();

        cart.getItems().add(cartItem);

        bookResponse = new BookResponse(
                10L,
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("550.00")
        );
    }

    @Test
    void getCart_shouldReturnExistingCart() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        when(bookService.getBooksByIds(List.of(10L)))
                .thenReturn(List.of(bookResponse));

        CartResponse response = cartService.getCart(100L);

        assertNotNull(response);
        assertEquals(1L, response.cartId());
        assertEquals(1, response.items().size());
        assertEquals(
                new BigDecimal("1100.00"),
                response.totalAmount()
        );

        verify(cartRepository).findByUserId(100L);
        verify(bookService).getBooksByIds(List.of(10L));
    }

    @Test
    void getCart_shouldReturnEmptyCartWhenCartDoesNotExist() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.empty());

        when(bookService.getBooksByIds(List.of()))
                .thenReturn(List.of());

        CartResponse response = cartService.getCart(100L);

        assertNotNull(response);
        assertEquals(0, response.items().size());
        assertEquals(BigDecimal.ZERO, response.totalAmount());

        verify(cartRepository).findByUserId(100L);
        verify(bookService).getBooksByIds(List.of());
    }

    @Test
    void addItem_shouldAddNewItemToCart() {

        var request = new AddCartItemRequest(10L, 2);

        when(bookService.getBookById(10L))
                .thenReturn(bookResponse);

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(bookService.getBooksByIds(List.of(10L)))
                .thenReturn(List.of(bookResponse));

        CartResponse response =
                cartService.addItem(100L, request);

        assertNotNull(response);
        assertEquals(1, response.items().size());
        assertEquals(2, response.items().get(0).quantity());

        verify(bookService).getBookById(10L);
        verify(cartRepository).findByUserId(100L);
        verify(cartRepository,times(2)).save(any(Cart.class));
    }

    @Test
    void addItem_shouldIncreaseQuantityWhenItemAlreadyExists() {

        cart.getItems().clear();

        var existingItem = CartItem.builder()
                .id(1L)
                .cart(cart)
                .bookId(10L)
                .quantity(2)
                .build();

        cart.getItems().add(existingItem);

        var request = new AddCartItemRequest(10L, 3);

        when(bookService.getBookById(10L))
                .thenReturn(bookResponse);

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        when(bookService.getBooksByIds(List.of(10L)))
                .thenReturn(List.of(bookResponse));

        CartResponse response =
                cartService.addItem(100L, request);

        assertEquals(5, existingItem.getQuantity());
        assertEquals(5, response.items().get(0).quantity());

        verify(bookService).getBookById(10L);
        verify(cartRepository).save(cart);
    }

    @Test
    void updateItem_shouldUpdateQuantity() {

        var request = new UpdateCartItemRequest(5);

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        when(bookService.getBooksByIds(List.of(10L)))
                .thenReturn(List.of(bookResponse));

        CartResponse response =
                cartService.updateItem(100L, 10L, request);

        assertEquals(5, cartItem.getQuantity());
        assertEquals(5, response.items().get(0).quantity());

        verify(cartRepository).findByUserId(100L);
        verify(bookService).getBooksByIds(List.of(10L));
    }

    @Test
    void updateItem_shouldThrowExceptionWhenItemNotFound() {

        var request = new UpdateCartItemRequest(5);

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.updateItem(
                        100L,
                        999L,
                        request
                )
        );

        assertEquals(
                "Book Not Found for bookId: 999",
                exception.getMessage()
        );

        verify(cartRepository).findByUserId(100L);
        verify(bookService, never())
                .getBooksByIds(any());
    }

    @Test
    void updateItem_shouldThrowExceptionWhenCartNotFound() {

        var request = new UpdateCartItemRequest(5);

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.updateItem(
                        100L,
                        10L,
                        request
                )
        );

        assertEquals(
                "Cart Not Found For user :100",
                exception.getMessage()
        );

        verify(cartRepository).findByUserId(100L);
    }

    @Test
    void removeItem_shouldRemoveItem() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        cartService.removeItem(100L, 10L);

        assertEquals(0, cart.getItems().size());

        verify(cartRepository).findByUserId(100L);
    }

    @Test
    void removeItem_shouldThrowExceptionWhenItemNotFound() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.removeItem(100L, 999L)
        );

        assertEquals(
                "book not found in cart for bookId: 999",
                exception.getMessage()
        );

        verify(cartRepository).findByUserId(100L);
    }

    @Test
    void removeItem_shouldThrowExceptionWhenCartNotFound() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.removeItem(100L, 10L)
        );

        assertEquals(
                "Cart Not Found For user :100",
                exception.getMessage()
        );

        verify(cartRepository).findByUserId(100L);
    }

    @Test
    void clearCart_shouldRemoveAllItems() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.of(cart));

        cartService.clearCart(100L);

        assertEquals(0, cart.getItems().size());

        verify(cartRepository).findByUserId(100L);
    }

    @Test
    void clearCart_shouldThrowExceptionWhenCartNotFound() {

        when(cartRepository.findByUserId(100L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.clearCart(100L)
        );

        assertEquals(
                "Cart Not Found For user :100",
                exception.getMessage()
        );

        verify(cartRepository).findByUserId(100L);
    }
}