package com.example.rest.bookstore.resources.resource;

import com.example.rest.bookstore.resources.exception.CustomerNotFoundException;
import com.example.rest.bookstore.resources.exception.OutOfStockException;
import com.example.rest.bookstore.resources.model.*;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final ConcurrentMap<Integer, List<Order>> customerOrders = new ConcurrentHashMap<>();
    private static final AtomicInteger orderIdCounter = new AtomicInteger(1);

    // Reference to CartResource and BookResource
    @Context
    private CartResource cartResource;
    @Context
    private BookResource bookResource;
    @Context
    private CustomerResource customerResource;
    
    @Inject
    public void setCartResource(CartResource cartResource) {
        this.cartResource = cartResource;
    }
    @Inject
    public void setBookResource(BookResource bookResource) {
        this.bookResource = bookResource;
    }
    @Inject
    public void setCustomerResource(CustomerResource customerResource) {
        this.customerResource = customerResource;
    }

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) {
        // Verify customer exists
        customerResource.getCustomer(customerId);

        // Get customer's cart
        Cart cart = cartResource.getCart(customerId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot create order from empty cart.");
        }

        // Process each item in cart
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {
            // Get book details
            Book book = bookResource.getBook(cartItem.getBookId());

            // Check stock again (in case it changed since adding to cart)
            if (!bookResource.checkStock(book.getId(), cartItem.getQuantity())) {
                throw new OutOfStockException(book.getId(), cartItem.getQuantity(), book.getStock());
            }

            // Reduce stock
            bookResource.reduceStock(book.getId(), cartItem.getQuantity());

            // Create order item
            OrderItem orderItem = new OrderItem(
                    book.getId(),
                    cartItem.getQuantity(),
                    book.getPrice()
            );

            orderItems.add(orderItem);
            totalAmount += book.getPrice() * cartItem.getQuantity();
        }

        // Create order
        Order order = new Order(
                orderIdCounter.getAndIncrement(),
                customerId,
                new Date(),
                orderItems,
                totalAmount
        );

        // Save order
        customerOrders.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);

        // Clear cart
        cartResource.clearCart(customerId);

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public List<Order> getCustomerOrders(@PathParam("customerId") int customerId) {
        // Verify customer exists
        customerResource.getCustomer(customerId);

        List<Order> orders = customerOrders.get(customerId);
        return orders != null ? orders : new ArrayList<>();
    }

    @GET
    @Path("/{orderId}")
    public Order getOrder(
            @PathParam("customerId") int customerId,
            @PathParam("orderId") int orderId) {
        // Verify customer exists
        customerResource.getCustomer(customerId);

        List<Order> orders = customerOrders.get(customerId);
        if (orders == null) {
            throw new IllegalArgumentException("No orders found for customer.");
        }

        return orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));
    }

    // Helper method for CustomerResource to delete customer's orders
    public void deleteCustomerOrders(int customerId) {
        customerOrders.remove(customerId);
    }
}