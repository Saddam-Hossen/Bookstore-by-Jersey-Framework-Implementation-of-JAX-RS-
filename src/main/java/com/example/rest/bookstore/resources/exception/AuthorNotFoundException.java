package com.example.rest.bookstore.resources.exception;
@SuppressWarnings("serial")
public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(int id) {
        super("Author with ID " + id + " does not exist.");
    }
}