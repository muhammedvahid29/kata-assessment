package com.bookstore.onlinebookstore.exception;

public class InsufficientStockException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7423515947602054844L;

	public InsufficientStockException(String message) {
		super(message);
	}
}
