package com.example.rest.bookstore.resources.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(int customerId) {
        super("Cart for customer with ID " + customerId + " does not exist.");
    }
}