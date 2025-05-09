package com.example.rest.bookstore.resources.resource;

import com.example.rest.bookstore.resources.exception.CustomerNotFoundException;
import com.example.rest.bookstore.resources.model.Customer;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final List<Customer> customers = new ArrayList<>();
    private static final AtomicInteger customerIdCounter = new AtomicInteger(1);

    // Reference to CartResource and OrderResource
    @Context
    private CartResource cartResource;
    @Context
    private OrderResource orderResource;
    
    @Inject
    public void setCartResource(CartResource cartResource) {
        this.cartResource = cartResource;
    }
    @Inject
    public void setOrderResource(OrderResource orderResource) {
        this.orderResource = orderResource;
    }

    @POST
    public Response createCustomer(Customer customer) {
        // Validate email format
        if (!customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate password
        if (customer.getPassword() == null || customer.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }

        // Set ID and add to list
        customer.setId(customerIdCounter.getAndIncrement());
        customers.add(customer);

        // Create a cart for the new customer
        cartResource.createCart(customer.getId());

        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public List<Customer> getAllCustomers() {
        return customers;
    }

    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        return customers.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        Customer existingCustomer = customers.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Validate email format
        if (!updatedCustomer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate password
        if (updatedCustomer.getPassword() == null || updatedCustomer.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }

        // Update fields
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPassword(updatedCustomer.getPassword());

        return existingCustomer;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer customerToRemove = customers.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Delete customer's cart and orders
        cartResource.deleteCart(id);
        orderResource.deleteCustomerOrders(id);

        customers.remove(customerToRemove);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}