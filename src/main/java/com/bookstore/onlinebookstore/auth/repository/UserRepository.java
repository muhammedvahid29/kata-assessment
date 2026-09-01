package com.bookstore.onlinebookstore.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.onlinebookstore.auth.entity.User;
import com.bookstore.onlinebookstore.auth.enums.Role;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByRole(Role role);
}
