package com.example.rest.bookstore.resources.exceptionMapper;


import com.example.rest.bookstore.resources.exception.CartNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
@Provider
public class CartNotFoundExceptionMapper  implements ExceptionMapper<CartNotFoundException>{
	 @Override
	    public Response toResponse(CartNotFoundException ex) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Cart not found");
	        error.put("message", ex.getMessage());

	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity(error)
	                       .build();
	    }

}
