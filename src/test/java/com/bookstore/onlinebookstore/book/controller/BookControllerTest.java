package com.bookstore.onlinebookstore.book.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bookstore.onlinebookstore.book.dto.BookResponse;
import com.bookstore.onlinebookstore.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookstore.onlinebookstore.book.dto.BookRequest;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {

        bookResponse = new BookResponse(
                1L,
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("550.00")
        );
    }

    @Test
    void getAllBooks_shouldReturnBooks() throws Exception {

        when(bookService.getAllBooks())
                .thenReturn(List.of(bookResponse));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title")
                        .value("Clean Code"))
                .andExpect(jsonPath("$[0].author")
                        .value("Robert C. Martin"))
                .andExpect(jsonPath("$[0].price")
                        .value(550.00));

        verify(bookService).getAllBooks();
    }

    @Test
    void getBookById_shouldReturnBook() throws Exception {

        when(bookService.getBookById(1L))
                .thenReturn(bookResponse);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Clean Code"))
                .andExpect(jsonPath("$.author")
                        .value("Robert C. Martin"))
                .andExpect(jsonPath("$.price")
                        .value(550.00));

        verify(bookService).getBookById(1L);
    }

    @Test
    void createBook_shouldReturnCreated() throws Exception {

        var request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("550.00")
        );

        when(bookService.createBook(request))
                .thenReturn(bookResponse);

        mockMvc.perform(
                post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title")
                .value("Clean Code"));

        verify(bookService).createBook(request);
    }

    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }
}