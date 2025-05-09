package com.example.rest.bookstore.resources.exceptionMapper;



import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

import com.example.rest.bookstore.resources.exception.InvalidInputException;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {

    @Override
    public Response toResponse(InvalidInputException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid input");
        error.put("message", ex.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(error)
                       .build();
    }
}
