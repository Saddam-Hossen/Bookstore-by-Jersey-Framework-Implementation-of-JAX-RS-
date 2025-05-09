package com.example.rest.bookstore.resources.resource;

import com.example.rest.bookstore.resources.exception.CartNotFoundException;
import com.example.rest.bookstore.resources.exception.OutOfStockException;
import com.example.rest.bookstore.resources.model.Cart;
import com.example.rest.bookstore.resources.model.CartItem;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static final ConcurrentMap<Integer, Cart> carts = new ConcurrentHashMap<>();

    // Reference to BookResource for stock checks
    @Context
    private BookResource bookResource;
    
    @Inject
    public void setBookResource(BookResource bookResource) {
        this.bookResource = bookResource;
    }

    public void createCart(int customerId) {
        carts.putIfAbsent(customerId, new Cart(customerId));
    }

    public void deleteCart(int customerId) {
        carts.remove(customerId);
    }

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        // Check if book exists and has enough stock
        if (!bookResource.checkStock(item.getBookId(), item.getQuantity())) {
            throw new OutOfStockException(item.getBookId(), item.getQuantity(),
                    bookResource.getBook(item.getBookId()).getStock());
        }

        // Check if item already exists in cart
        CartItem existingItem = cart.findItem(item.getBookId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            cart.addItem(item);
        }

        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        return cart;
    }

    @PUT
    @Path("/items/{bookId}")
    public Cart updateCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId,
            CartItem updatedItem) {
        if (bookId != updatedItem.getBookId()) {
            throw new IllegalArgumentException("Book ID in path and body must match.");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        // Check if book exists and has enough stock
        if (!bookResource.checkStock(bookId, updatedItem.getQuantity())) {
            throw new OutOfStockException(bookId, updatedItem.getQuantity(),
                    bookResource.getBook(bookId).getStock());
        }

        // Find and update item
        CartItem existingItem = cart.findItem(bookId);
        if (existingItem == null) {
            throw new IllegalArgumentException("Item not found in cart.");
        }

        existingItem.setQuantity(updatedItem.getQuantity());

        return cart;
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        cart.removeItem(bookId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Helper method for OrderResource to clear cart after order is placed
    public void clearCart(int customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.getItems().clear();
        }
    }
}