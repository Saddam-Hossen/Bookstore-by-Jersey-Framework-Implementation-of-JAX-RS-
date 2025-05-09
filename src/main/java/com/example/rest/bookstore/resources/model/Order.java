package com.example.rest.bookstore.resources.model;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private Date orderDate;
    private List<OrderItem> items = new ArrayList<>();
    private double totalAmount;

    public Order() {}

    public Order(int id, int customerId, Date orderDate, List<OrderItem> items, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    @JsonbProperty("id")
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @JsonbProperty("customerId")
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    @JsonbProperty("orderDate")
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    @JsonbProperty("items")
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    @JsonbProperty("totalAmount")
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}