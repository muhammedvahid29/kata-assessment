package com.bookstore.onlinebookstore.cart.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bookstore.onlinebookstore.cart.dto.AddCartItemRequest;
import com.bookstore.onlinebookstore.cart.dto.CartItemResponse;
import com.bookstore.onlinebookstore.cart.dto.CartResponse;
import com.bookstore.onlinebookstore.cart.dto.UpdateCartItemRequest;
import com.bookstore.onlinebookstore.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartResponse cartResponse;

    @BeforeEach
    void setUp() {

        var item = new CartItemResponse(
                10L,
                "Clean Code",
                new BigDecimal("550.00"),
                2,
                new BigDecimal("1100.00")
        );

        cartResponse = new CartResponse(
                1L,
                List.of(item),
                new BigDecimal("1100.00")
        );
    }

    @Test
    void getCart_shouldReturnCart() throws Exception {

        when(cartService.getCart(100L))
                .thenReturn(cartResponse);

        mockMvc.perform(
                get("/api/cart/100")
        )
        .andExpect(status().isOk());

        verify(cartService)
                .getCart(100L);
    }

    @Test
    void addItem_shouldReturnCreated() throws Exception {

        var request = new AddCartItemRequest(
                10L,
                2
        );

        when(cartService.addItem(100L, request))
                .thenReturn(cartResponse);

        mockMvc.perform(
                post("/api/cart/100/items")
                        .contentType("application/json")
                        .content(
                            objectMapper.writeValueAsString(request)
                        )
        )
        .andExpect(status().isCreated());

        verify(cartService)
                .addItem(100L, request);
    }

    @Test
    void updateItem_shouldReturnUpdatedCart() throws Exception {

        var request = new UpdateCartItemRequest(5);

        when(cartService.updateItem(
                100L,
                10L,
                request
        )).thenReturn(cartResponse);

        mockMvc.perform(
                put("/api/cart/100/items/10")
                        .contentType("application/json")
                        .content(
                            objectMapper.writeValueAsString(request)
                        )
        )
        .andExpect(status().isOk());

        verify(cartService)
                .updateItem(100L, 10L, request);
    }

    @Test
    void removeItem_shouldReturnNoContent() throws Exception {

        mockMvc.perform(
                delete("/api/cart/100/items/10")
        )
        .andExpect(status().isNoContent());

        verify(cartService)
                .removeItem(100L, 10L);
    }

    @Test
    void clearCart_shouldReturnNoContent() throws Exception {

        mockMvc.perform(
                delete("/api/cart/100/items")
        )
        .andExpect(status().isNoContent());

        verify(cartService)
                .clearCart(100L);
    }
}