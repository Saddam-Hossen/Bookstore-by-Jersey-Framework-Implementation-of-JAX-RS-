package com.example.rest.bookstore.resources.model;

import jakarta.json.bind.annotation.JsonbProperty;

public class OrderItem {
    private int bookId;
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(int bookId, int quantity, double price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    @JsonbProperty("bookId")
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    @JsonbProperty("quantity")
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @JsonbProperty("price")
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}