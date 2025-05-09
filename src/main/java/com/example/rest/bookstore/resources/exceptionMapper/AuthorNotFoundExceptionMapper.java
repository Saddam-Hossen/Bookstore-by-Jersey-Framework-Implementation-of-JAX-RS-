package com.example.rest.bookstore.resources.exceptionMapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

import com.example.rest.bookstore.resources.exception.AuthorNotFoundException;

@Provider
public class AuthorNotFoundExceptionMapper implements ExceptionMapper<AuthorNotFoundException> {

    @Override
    public Response toResponse(AuthorNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Author not found");
        error.put("message", ex.getMessage());

        return Response.status(Response.Status.NOT_FOUND) // or NOT_FOUND
                       .entity(error)
                       .build();
    }
}
