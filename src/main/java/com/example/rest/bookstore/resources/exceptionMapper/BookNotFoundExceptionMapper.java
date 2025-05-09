package com.example.rest.bookstore.resources.exceptionMapper;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

import com.example.rest.bookstore.resources.exception.BookNotFoundException;

@Provider
public class BookNotFoundExceptionMapper implements ExceptionMapper<BookNotFoundException> {

    @Override
    public Response toResponse(BookNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Book not found");
        error.put("message", ex.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                       .entity(error)
                       .build();
    }
}
