package com.example.rest.bookstore.resources.exceptionMapper;

import com.example.rest.bookstore.resources.exception.CustomerNotFoundException;
import java.util.HashMap;
import java.util.Map;



import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class CustomerNotFoundExceptionMapper implements ExceptionMapper<CustomerNotFoundException>{
	 @Override
	    public Response toResponse(CustomerNotFoundException ex) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Customer not found");
	        error.put("message", ex.getMessage());

	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity(error)
	                       .build();
	    }
}
