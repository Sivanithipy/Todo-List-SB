// src/main/java/com/example/todo_app/config/CorsConfig.java

package com.example.todo_app.config; // Ensure this matches your config package

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for the Spring Boot application.
 * This class configures Cross-Origin Resource Sharing to allow
 * requests from specific origins (your frontend).
 */
@Configuration // Marks this class as a Spring configuration class
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS to all API endpoints under /api/
                .allowedOrigins("http://localhost:63342") // Allow requests from your frontend's origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow sending cookies/authentication headers
    }
}
