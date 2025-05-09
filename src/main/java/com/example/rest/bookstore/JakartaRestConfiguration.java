package com.example.rest.bookstore;

import org.glassfish.jersey.server.ResourceConfig;
import jakarta.ws.rs.ApplicationPath;
import com.example.rest.bookstore.resources.*;

@ApplicationPath("/api")
public class JakartaRestConfiguration extends ResourceConfig {
    public JakartaRestConfiguration() {
        packages("com.example.rest.bookstore.resources");
    }
}

