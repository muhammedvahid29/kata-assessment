package com.bookstore.onlinebookstore.auth.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bookstore.onlinebookstore.auth.dto.RegisterRequest;
import com.bookstore.onlinebookstore.auth.dto.UserResponse;
import com.bookstore.onlinebookstore.auth.enums.Role;
import com.bookstore.onlinebookstore.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest(
                "customer@gmail.com",
                "password123"
        );

        userResponse = new UserResponse(
                1L,
                "customer@gmail.com",
                Role.CUSTOMER
        );
    }

    @Test
    void register_shouldReturnCreated() throws Exception {

        when(authService.register(registerRequest))
                .thenReturn(userResponse);

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType("application/json")
                        .content(
                                objectMapper.writeValueAsString(
                                        registerRequest
                                )
                        )
        )
        .andExpect(status().isCreated());

        verify(authService)
                .register(registerRequest);
    }
}