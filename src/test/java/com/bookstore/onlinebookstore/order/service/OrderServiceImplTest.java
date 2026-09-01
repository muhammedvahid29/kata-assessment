package com.bookstore.onlinebookstore.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.onlinebookstore.cart.dto.CartItemResponse;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.service.CartService;
import com.bookstore.onlinebookstore.exception.InsufficientStockException;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;
import com.bookstore.onlinebookstore.inventory.service.InventoryService;
import com.bookstore.onlinebookstore.order.dto.OrderResponse;
import com.bookstore.onlinebookstore.order.entity.Order;
import com.bookstore.onlinebookstore.order.entity.OrderItem;
import com.bookstore.onlinebookstore.order.enums.OrderStatus;
import com.bookstore.onlinebookstore.order.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private CartItemResponse cartItem;
    private CartResponse cart;
    private Order order;

    @BeforeEach
    void setUp() {

        cartItem = new CartItemResponse(
                10L,
                "Clean Code",
                new BigDecimal("550.00"),
                2,
                new BigDecimal("1100.00")
        );

        cart = new CartResponse(
                1L,
                List.of(cartItem),
                new BigDecimal("1100.00")
        );

        var orderItem = OrderItem.builder()
                .bookId(10L)
                .bookTitle("Clean Code")
                .price(new BigDecimal("550.00"))
                .quantity(2)
                .subtotal(new BigDecimal("1100.00"))
                .build();

        order = Order.builder()
                .id(1L)
                .userId(100L)
                .status(OrderStatus.PLACED)
                .createdAt(LocalDateTime.now())
                .totalAmount(new BigDecimal("1100.00"))
                .items(new ArrayList<>(List.of(orderItem)))
                .build();
    }

    @Test
    void placeOrder_shouldPlaceOrderSuccessfully() {

        when(cartService.getCart(100L))
                .thenReturn(cart);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderResponse response =
                orderService.placeOrder(100L);

        assertNotNull(response);
        assertEquals(1L, response.orderId());
        assertEquals(100L, response.userId());
        assertEquals(OrderStatus.PLACED, response.status());
        assertEquals(
                new BigDecimal("1100.00"),
                response.totalAmount()
        );

        verify(cartService).getCart(100L);

        verify(inventoryService)
                .decreaseStock(10L, 2);

        verify(orderRepository)
                .save(any(Order.class));

        verify(cartService)
                .clearCart(100L);
    }

    @Test
    void placeOrder_shouldThrowExceptionWhenCartIsEmpty() {

        var emptyCart = new CartResponse(
                1L,
                List.of(),
                BigDecimal.ZERO
        );

        when(cartService.getCart(100L))
                .thenReturn(emptyCart);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.placeOrder(100L)
        );

        assertEquals(
                "Cannot Place order with a empty cart",
                exception.getMessage()
        );

        verify(cartService).getCart(100L);

        verify(inventoryService, never())
                .decreaseStock(any(), any(Integer.class));

        verify(orderRepository, never())
                .save(any(Order.class));

        verify(cartService, never())
                .clearCart(100L);
    }

    @Test
    void placeOrder_shouldFailWhenStockIsInsufficient() {

        when(cartService.getCart(100L))
                .thenReturn(cart);

        doThrow(new InsufficientStockException(
                "Insufficient stock for book: 10"
        )).when(inventoryService)
                .decreaseStock(10L, 2);

        assertThrows(
                InsufficientStockException.class,
                () -> orderService.placeOrder(100L)
        );

        verify(cartService).getCart(100L);

        verify(inventoryService)
                .decreaseStock(10L, 2);

        verify(orderRepository, never())
                .save(any(Order.class));

        verify(cartService, never())
                .clearCart(100L);
    }

    @Test
    void getOrderById_shouldReturnOrder() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response =
                orderService.getOrderById(100L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.orderId());
        assertEquals(100L, response.userId());
        assertEquals(
                OrderStatus.PLACED,
                response.status()
        );
        assertEquals(
                new BigDecimal("1100.00"),
                response.totalAmount()
        );

        assertEquals(1, response.items().size());

        verify(orderRepository)
                .findById(1L);
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenOrderNotFound() {

        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrderById(100L, 999L)
        );

        assertEquals(
                "Order not found with id: 999",
                exception.getMessage()
        );

        verify(orderRepository)
                .findById(999L);
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenOrderBelongsToAnotherUser() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrderById(200L, 1L)
        );

        assertEquals(
                "Order not found with id: 1",
                exception.getMessage()
        );

        verify(orderRepository)
                .findById(1L);
    }

    @Test
    void getMyOrders_shouldReturnUserOrders() {

        when(orderRepository
                .findByUserIdOrderByCreatedAtDesc(100L))
                .thenReturn(List.of(order));

        List<OrderResponse> responses =
                orderService.getMyOrders(100L);

        assertNotNull(responses);
        assertEquals(1, responses.size());

        var response = responses.get(0);

        assertEquals(1L, response.orderId());
        assertEquals(100L, response.userId());
        assertEquals(
                OrderStatus.PLACED,
                response.status()
        );
        assertEquals(
                new BigDecimal("1100.00"),
                response.totalAmount()
        );

        verify(orderRepository)
                .findByUserIdOrderByCreatedAtDesc(100L);
    }

    @Test
    void getMyOrders_shouldReturnEmptyListWhenNoOrders() {

        when(orderRepository
                .findByUserIdOrderByCreatedAtDesc(100L))
                .thenReturn(List.of());

        List<OrderResponse> responses =
                orderService.getMyOrders(100L);

        assertNotNull(responses);
        assertEquals(0, responses.size());

        verify(orderRepository)
                .findByUserIdOrderByCreatedAtDesc(100L);
    }
}