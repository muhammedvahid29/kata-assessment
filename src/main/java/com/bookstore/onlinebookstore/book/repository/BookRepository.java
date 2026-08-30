package com.bookstore.onlinebookstore.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.onlinebookstore.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
