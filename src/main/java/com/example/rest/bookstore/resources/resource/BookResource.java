package com.example.rest.bookstore.resources.resource;


import com.example.rest.bookstore.resources.exception.*;

import com.example.rest.bookstore.resources.model.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static final List<Book> books = new ArrayList<>();
    private static final AtomicInteger bookIdCounter = new AtomicInteger(1);
    private AuthorResource authorResource;
    
    @Inject
    public void setAuthorResource(AuthorResource authorResource) {
        this.authorResource = authorResource;
    }

    @POST
    public Response createBook(Book book) {
        // Validate author exists
    	System.out.println("author book "+ book.getAuthorId());
    	//System.out.print("Author "+authorResource.getAllAuthors());
        try {
            authorResource.getAuthor(book.getAuthorId());
        } catch (AuthorNotFoundException e) {
            throw e;
        }
    	System.out.println("Not pass it");

        // Validate publication year
        if (book.getPublicationYear() > java.time.Year.now().getValue()) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }

        // Validate stock
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative.");
        }

        // Validate price
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be positive.");
        }

        // Set ID and add to list
        book.setId(bookIdCounter.getAndIncrement());
        books.add(book);

        // Add book to author's book list
        authorResource.addBookToAuthor(book.getAuthorId(), book.getId());

        return Response.status(Response.Status.CREATED).entity(book).build();
    }


    @GET
    public List<Book> getAllBooks() {
        //System.out.println("Books size: " + books.size());
        for (Book b : books) {
            System.out.println("Book: " + b.getTitle() + ", Author ID: " + b.getAuthorId());
        }
        return books;
    }

    @GET
    @Path("/{id}")
    public Book getBook(@PathParam("id") int id) {
       // System.out.println("Getting book with ID: " + id);
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") int id, Book updatedBook) {
        Book existingBook = books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));

        // Validate author exists if changed
        if (updatedBook.getAuthorId() != existingBook.getAuthorId()) {
            try {
                authorResource.getAuthor(updatedBook.getAuthorId());
            } catch (AuthorNotFoundException e) {
                throw e;
            }

            // Update author's book list
            authorResource.removeBookFromAuthor(existingBook.getAuthorId(), id);
            authorResource.addBookToAuthor(updatedBook.getAuthorId(), id);
        }

        // Validate publication year
        if (updatedBook.getPublicationYear() > java.time.Year.now().getValue()) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }

        // Update fields
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthorId(updatedBook.getAuthorId());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setStock(updatedBook.getStock());

        return existingBook;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book bookToRemove = books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));

        books.remove(bookToRemove);

        // Remove book from author's book list
        authorResource.removeBookFromAuthor(bookToRemove.getAuthorId(), id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Helper method for CartResource to check stock
    public boolean checkStock(int bookId, int quantity) {
        Book book = books.stream()
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(bookId));

        return book.getStock() >= quantity;
    }

    // Helper method for CartResource to reduce stock
    public void reduceStock(int bookId, int quantity) {
        Book book = books.stream()
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getStock() < quantity) {
            throw new OutOfStockException(bookId, quantity, book.getStock());
        }

        book.setStock(book.getStock() - quantity);
    }
}