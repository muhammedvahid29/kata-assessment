package com.bookstore.onlinebookstore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExecptionHandlerTest {

    private GlobalExecptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExecptionHandler();
    }

    @Test
    void handleNotFound_shouldReturn404() {

        var exception = new ResourceNotFoundException(
                "Book not found with id: 10"
        );

        var response =
                exceptionHandler.handleNotFound(exception);

        assertNotNull(response);
        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        var body = response.getBody();

        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("NOT_FOUND", body.error());
        assertEquals(
                "Book not found with id: 10",
                body.message()
        );
        assertNotNull(body.timestamp());
    }

    @Test
    void handleIllegalArgument_shouldReturn400() {

        var exception = new IllegalArgumentException(
                "Email already registered"
        );

        var response =
                exceptionHandler.handleIllegalArgument(exception);

        assertNotNull(response);
        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        var body = response.getBody();

        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("BAD_REQUEST", body.error());
        assertEquals(
                "Email already registered",
                body.message()
        );
        assertNotNull(body.timestamp());
    }

}