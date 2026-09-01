package com.bookstore.onlinebookstore.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.onlinebookstore.book.dto.BookRequest;
import com.bookstore.onlinebookstore.book.entity.Book;
import com.bookstore.onlinebookstore.book.repository.BookRepository;
import com.bookstore.onlinebookstore.exception.ResourceNotFoundException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookServiceImplTest {
	@Mock
	BookRepository repository;
	
	@InjectMocks
	BookServiceImpl bookService;
	
	Book book1;
	Book book2;
	BookRequest request;
	
	@BeforeEach
	void setup() {
		request = new BookRequest(
				"Clean Code",
				"James",
				new BigDecimal("550.00")
		);
		
		book1 = Book.builder()
					.bookId(1L)
					.title("Clean Code")
					.author("James")
					.price(new BigDecimal("550.00"))
					.build();
		
		book2 =  Book.builder()
				.bookId(2L)
				.title("Java programming")
				.author("nolan")
				.price(new BigDecimal("1000.23"))
				.build();
	}
	
	@Test
	void createBook_shouldCreateAndReturnBook() {
		
		when(repository.save(any(Book.class))).thenReturn(book1);
		
		var response = bookService.createBook(request);
		
		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals(request.title(), response.title());
		assertEquals(request.author(), response.author());
		assertEquals(request.price(), response.price());
		
		verify(repository,times(1)).save(any(Book.class));
	}
	
	@Test
	void getBookById_shouldReturnBook() {
		
		when(repository.findById(1L)).thenReturn(Optional.of(book1));
		
		var response = bookService.getBookById(1L);
		
		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("Clean Code", response.title());
		assertEquals("James", response.author());
		assertEquals(new BigDecimal("550.00"), response.price());
		
		verify(repository,times(1)).findById(anyLong());
	}
	
	@Test
	void getBookById_shouldThrowResourceNotFoundException() {
		when(repository.findById(1L)).thenReturn(Optional.empty());
		
		var exception = assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
		
		assertEquals("Book not found with id: 1", exception.getMessage());
		
		verify(repository,times(1)).findById(anyLong());
	}
	
	@Test
	void getBookByIds_shouldReturnBooks() {
		
		when(repository.findAllById(List.of(1L,2L))).thenReturn(List.of(book1,book2));
		
		var response = bookService.getBooksByIds(List.of(1L,2L));
		
		assertNotNull(response);
		assertEquals(2, response.size());
		
		assertEquals(1L, response.get(0).id());
		assertEquals("Clean Code", response.get(0).title());
		
		assertEquals(2L, response.get(1).id());
		assertEquals("Java programming", response.get(1).title());
		
		verify(repository).findAllById(List.of(1L,2L));
	}
	
	@Test
	void getAllBooks_ShouldReturnAllBooks() {
		when(repository.findAll()).thenReturn(List.of(book1,book2));
		
		var response = bookService.getAllBooks();
		
		assertNotNull(response);
		assertEquals(2, response.size());
		
		assertEquals(1L, response.get(0).id());
		assertEquals("James", response.get(0).author());
		
		assertEquals(2L, response.get(1).id());
		assertEquals("nolan", response.get(1).author());
		
		verify(repository).findAll();
	}
	
	@Test
	void updateBook_ShouldUpdateAndReturnBook() {
		var request = new BookRequest(
                "Clean Code - Updated",
                "Robert C. Martin",
                new BigDecimal("600.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(book1));

        var response =
                bookService.updateBook(1L, request);

        assertNotNull(response);

        assertEquals(1L, response.id());
        assertEquals(
                "Clean Code - Updated",
                response.title()
        );
        assertEquals(
                "Robert C. Martin",
                response.author()
        );
        assertEquals(
                new BigDecimal("600.00"),
                response.price()
        );

        verify(repository).findById(1L);
	}
	
	 @Test
	 void updateBook_shouldThrowExceptionWhenBookNotFound() {
		 var request = new BookRequest(
	                "Updated Book",
	                "Author",
	                new BigDecimal("500.00")
	        );

	        when(repository.findById(1L))
	                .thenReturn(Optional.empty());

	        var exception = assertThrows(
	                ResourceNotFoundException.class,
	                () -> bookService.updateBook(1L, request)
	        );

	        assertEquals(
	                "Book not found with id: 1",
	                exception.getMessage()
	        );

	        verify(repository).findById(1L); 
	 }
}
