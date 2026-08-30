package com.bookstore.onlinebookstore.book.service;

import java.util.List;

import com.bookstore.onlinebookstore.book.dto.BookRequest;
import com.bookstore.onlinebookstore.book.dto.BookResponse;

public interface BookService {
	BookResponse createBook(BookRequest bookRequest);
	BookResponse getBookById(Long id);
	List<BookResponse> getAllBooks();
	BookResponse updateBook(Long id, BookRequest bookRequest);
	void deleteBook(Long id);
}
