package com.example.rest.bookstore.resources.model;

import jakarta.json.bind.annotation.JsonbProperty;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String password;

    // Constructors, getters, and setters
    public Customer() {}

    public Customer(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @JsonbProperty("id")
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @JsonbProperty("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonbProperty("email")
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @JsonbProperty("password")
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}