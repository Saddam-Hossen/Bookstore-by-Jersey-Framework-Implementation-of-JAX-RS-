package com.example.rest.bookstore.resources.exceptionMapper;

import com.example.rest.bookstore.resources.exception.OutOfStockException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException>{
	 @Override
	    public Response toResponse(OutOfStockException ex) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Out Of Stock");
	        error.put("message", ex.getMessage());

	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity(error)
	                       .build();
	    }

}