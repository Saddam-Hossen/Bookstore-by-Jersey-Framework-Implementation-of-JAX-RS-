package com.example.rest.bookstore.resources.model;

import com.example.rest.bookstore.resources.model.CartItem;
import jakarta.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> items = new ArrayList<>();

    public Cart(int customerId) {
        this.customerId = customerId;
    }

    @JsonbProperty("customerId")
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    @JsonbProperty("items")
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(int bookId) {
        items.removeIf(item -> item.getBookId() == bookId);
    }

    public CartItem findItem(int bookId) {
        return items.stream()
                .filter(item -> item.getBookId() == bookId)
                .findFirst()
                .orElse(null);
    }
}