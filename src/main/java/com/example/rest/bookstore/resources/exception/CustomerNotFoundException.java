package com.example.rest.bookstore.resources.exception;
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(int id) {
        super("Customer with ID " + id + " does not exist.");
    }
}