package com.example.rest.bookstore.resources.model;

import jakarta.json.bind.annotation.JsonbProperty;

public class CartItem {
    private int bookId;
    private int quantity;

    public CartItem() {}

    public CartItem(int bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    @JsonbProperty("bookId")
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    @JsonbProperty("quantity")
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}