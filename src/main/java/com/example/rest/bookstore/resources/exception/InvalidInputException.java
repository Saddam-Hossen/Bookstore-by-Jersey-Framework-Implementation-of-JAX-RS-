package com.example.rest.bookstore.resources.exception;
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}