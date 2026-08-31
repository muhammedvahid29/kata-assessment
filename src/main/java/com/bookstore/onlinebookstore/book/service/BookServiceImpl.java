package com.bookstore.onlinebookstore.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.onlinebookstore.book.dto.BookRequest;
import com.bookstore.onlinebookstore.book.dto.BookResponse;
import com.bookstore.onlinebookstore.book.entity.Book;
import com.bookstore.onlinebookstore.book.repository.BookRepository;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
	
	BookRepository bookRepository;

	@Override
	public BookResponse createBook(BookRequest bookRequest) {
		var book = Book.builder()
					.title(bookRequest.title())
					.author(bookRequest.author())
					.price(bookRequest.price())
					.build();
		
		var savedBook = bookRepository.save(book);
		
		return toResponse(savedBook);
	}

	@Override
	@Transactional(readOnly = true)
	public BookResponse getBookById(Long id) {
		
		var book = findBook(id);
		
		return toResponse(book);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BookResponse> getBooksByIds(List<Long> ids) {
        return bookRepository.findAllById(ids)
        				.stream()
        				.map(book -> { 
        					return new BookResponse(
        							book.getBookId(),
        							book.getTitle(),
        							book.getAuthor(),
        							book.getPrice());
        				}).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookResponse> getAllBooks() {
		return bookRepository.findAll()
				.stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	public BookResponse updateBook(Long id, BookRequest bookRequest) {
		
		var book = findBook(id);
		
		book.setTitle(bookRequest.title());
		book.setAuthor(bookRequest.author());
		book.setPrice(bookRequest.price());
		return toResponse(book);
	}

	@Override
	public void deleteBook(Long id) {
		
		var book = findBook(id);
		
		bookRepository.delete(book);
	}
	
	private Book findBook(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> 
					new ResourceNotFoundException(
							"Book not found with id: "+id
							));
	}
	
	private BookResponse toResponse(Book book) {
		return new BookResponse(
				book.getBookId(),
				book.getTitle(),
				book.getAuthor(),
				book.getPrice()
			);
	}

}
