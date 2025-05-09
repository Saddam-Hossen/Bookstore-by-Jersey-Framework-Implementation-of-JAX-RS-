package com.example.rest.bookstore.resources.exception;
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Book with ID " + id + " does not exist.");
    }
}