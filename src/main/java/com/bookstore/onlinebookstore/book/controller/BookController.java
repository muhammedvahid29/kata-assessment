package com.bookstore.onlinebookstore.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.onlinebookstore.book.dto.BookRequest;
import com.bookstore.onlinebookstore.book.dto.BookResponse;
import com.bookstore.onlinebookstore.book.service.BookService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/books")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class BookController {
     BookService bookService;
     
     @PreAuthorize("hasRole('ADMIN')")
     @PostMapping
     public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest){
    	 return ResponseEntity
    			 .status(HttpStatus.CREATED)
    			 .body(bookService.createBook(bookRequest));
     }
     
     @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
     @GetMapping
     public ResponseEntity<List<BookResponse>> getAllBooks() {
    	 return ResponseEntity.ok(
    			 	bookService.getAllBooks()
    			 );
     }
     
     @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
     @GetMapping("/{id}")
     public ResponseEntity<BookResponse> getBookById(
    		   @PathVariable Long id
    		 ){
    	 
    	 return ResponseEntity.ok(bookService.getBookById(id));
     }
     
     @PreAuthorize("hasRole('ADMIN')")
     @PutMapping("/{id}")
     public ResponseEntity<BookResponse> updateBook(
             @PathVariable Long id,
             @Valid @RequestBody BookRequest bookRequest) {

         return ResponseEntity.ok(
                 bookService.updateBook(id, bookRequest)
         );
     }
     
     @PreAuthorize("hasRole('ADMIN')")
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    	 bookService.deleteBook(id);
    	 
    	 return ResponseEntity.noContent().build();
     }
}
