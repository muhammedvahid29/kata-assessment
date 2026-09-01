package com.bookstore.onlinebookstore.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bookstore.onlinebookstore.auth.dto.RegisterRequest;
import com.bookstore.onlinebookstore.auth.entity.User;
import com.bookstore.onlinebookstore.auth.enums.Role;
import com.bookstore.onlinebookstore.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private User user;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest(
                "customer@gmail.com",
                "password123"
        );

        user = User.builder()
                .id(1L)
                .email("customer@gmail.com")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    void register_shouldCreateCustomerSuccessfully() {

        when(userRepository.existsByEmail(
                registerRequest.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(
                registerRequest.password()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        var response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(
                "customer@gmail.com",
                response.email()
        );
        assertEquals(
                Role.CUSTOMER,
                response.role()
        );

        verify(userRepository)
                .existsByEmail("customer@gmail.com");

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void register_shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(
                registerRequest.email()))
                .thenReturn(true);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals(
                "Email already registered",
                exception.getMessage()
        );

        verify(userRepository)
                .existsByEmail("customer@gmail.com");

        verify(passwordEncoder, never())
                .encode(any());

        verify(userRepository, never())
                .save(any(User.class));
    }
}