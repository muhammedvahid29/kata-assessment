package com.bookstore.onlinebookstore.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bookstore.onlinebookstore.auth.entity.User;
import com.bookstore.onlinebookstore.auth.enums.Role;
import com.bookstore.onlinebookstore.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .email("customer@gmail.com")
                .password("$2a$10$encodedPassword")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {

        when(userRepository.findByEmail("customer@gmail.com"))
                .thenReturn(Optional.of(user));

        var userDetails =
                userDetailsService.loadUserByUsername(
                        "customer@gmail.com"
                );

        assertEquals(
                "customer@gmail.com",
                userDetails.getUsername()
        );

        assertEquals(
                "$2a$10$encodedPassword",
                userDetails.getPassword()
        );

        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_CUSTOMER")
                        )
        );

        verify(userRepository)
                .findByEmail("customer@gmail.com");
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(
                        "unknown@gmail.com"
                )
        );

        assertEquals(
                "User not found: unknown@gmail.com",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail("unknown@gmail.com");
    }
}