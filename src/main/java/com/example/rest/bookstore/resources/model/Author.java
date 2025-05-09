package com.example.rest.bookstore.resources.model;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

public class Author {
    private int id;
    private String name;
    private String biography;
    private List<Integer> bookIds = new ArrayList<>();

    // Constructors, getters, and setters
    public Author() {}

    public Author(int id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    @JsonbProperty("id")
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @JsonbProperty("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonbProperty("biography")
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    @JsonbProperty("bookIds")
    public List<Integer> getBookIds() { return bookIds; }
    public void setBookIds(List<Integer> bookIds) { this.bookIds = bookIds; }

    public void addBook(int bookId) {
        if (!bookIds.contains(bookId)) {
            bookIds.add(bookId);
        }
    }

    public void removeBook(int bookId) {
        bookIds.remove((Integer) bookId);
    }
}