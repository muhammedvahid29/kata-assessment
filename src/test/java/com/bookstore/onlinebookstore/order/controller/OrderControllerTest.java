package com.bookstore.onlinebookstore.order.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bookstore.onlinebookstore.order.dto.OrderResponse;
import com.bookstore.onlinebookstore.order.enums.OrderStatus;
import com.bookstore.onlinebookstore.order.service.OrderService;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {

        orderResponse = new OrderResponse(
                1L,
                100L,
                OrderStatus.PLACED,
                new BigDecimal("1100.00"),
                LocalDateTime.now(),
                List.of()
        );
    }

    @Test
    void placeOrder_shouldReturnCreated() throws Exception {

        when(orderService.placeOrder(100L))
                .thenReturn(orderResponse);

        mockMvc.perform(
                post("/api/orders")
                .header("X-USER-ID", 100L)
        )
        .andExpect(status().isCreated());

        verify(orderService)
                .placeOrder(100L);
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {

        when(orderService.getOrderById(100L, 1L))
                .thenReturn(orderResponse);

        mockMvc.perform(
                get("/api/orders/1")
                .header("X-USER-ID", 100L)
        )
        .andExpect(status().isOk());

        verify(orderService)
                .getOrderById(100L, 1L);
    }

    @Test
    void getMyOrders_shouldReturnOrders() throws Exception {

        when(orderService.getMyOrders(100L))
                .thenReturn(List.of(orderResponse));

        mockMvc.perform(
                get("/api/orders")
                .header("X-USER-ID", 100L)
        )
        .andExpect(status().isOk());

        verify(orderService)
                .getMyOrders(100L);
    }
}