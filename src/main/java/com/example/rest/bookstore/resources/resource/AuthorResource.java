package com.example.rest.bookstore.resources.resource;


import com.example.rest.bookstore.resources.exception.AuthorNotFoundException;
import com.example.rest.bookstore.resources.exception.BookNotFoundException;
import com.example.rest.bookstore.resources.model.Author;
import com.example.rest.bookstore.resources.model.Book;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final List<Author> authors = new ArrayList<>();
    private static final AtomicInteger authorIdCounter = new AtomicInteger(1);

    // Reference to BookResource for book operations
    private BookResource bookResource;
    
    

    @POST
    public Response createAuthor(Author author) {
        // Validate name
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }

        // Set ID and add to list
        author.setId(authorIdCounter.getAndIncrement());
        authors.add(author);

        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public List<Author> getAllAuthors() {
        return authors;
    }

    @GET
    @Path("/{id}")
    public Author getAuthor(@PathParam("id") int id) {
        return authors.stream()
                .filter(author -> author.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }
    

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        Author existingAuthor = authors.stream()
                .filter(author -> author.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));

        // Validate name
        if (updatedAuthor.getName() == null || updatedAuthor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }

        // Update fields
        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setBiography(updatedAuthor.getBiography());

        return existingAuthor;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author authorToRemove = authors.stream()
                .filter(author -> author.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));

        // Check if author has books
        if (!authorToRemove.getBookIds().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete author with existing books.");
        }

        authors.remove(authorToRemove);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/{id}/books")
    public List<Book> getAuthorBooks(@PathParam("id") int id) {
        Author author = authors.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));

        List<Book> authorBooks = new ArrayList<>();
        for (int bookId : author.getBookIds()) {
            try {
                authorBooks.add(bookResource.getBook(bookId));
            } catch (BookNotFoundException e) {
                // Skip books that might have been deleted
            }
        }

        return authorBooks;
    }

    // Helper method for BookResource to add book to author
    public void addBookToAuthor(int authorId, int bookId) {
        Author author = authors.stream()
                .filter(a -> a.getId() == authorId)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        author.addBook(bookId);
    }

    // Helper method for BookResource to remove book from author
    public void removeBookFromAuthor(int authorId, int bookId) {
        Author author = authors.stream()
                .filter(a -> a.getId() == authorId)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        author.removeBook(bookId);
    }
}