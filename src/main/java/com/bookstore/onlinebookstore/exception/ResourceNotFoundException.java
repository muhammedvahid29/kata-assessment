package com.bookstore.onlinebookstore.exception;


public class ResourceNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9093309588824662934L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
