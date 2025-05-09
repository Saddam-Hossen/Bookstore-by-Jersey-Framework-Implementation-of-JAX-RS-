package com.example.rest.bookstore.resources.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(int bookId, int requested, int available) {
        super("Book with ID " + bookId + " has only " + available + " items in stock (requested: " + requested + ").");
    }
}