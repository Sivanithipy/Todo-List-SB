// src/main/java/com/example/todoapp/model/Todo.java

package com.example.todo_app.model; // Corrected package name: removed underscore

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data; // Import Lombok annotations
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate; // For dueDate
import java.time.LocalDateTime; // For createdAt, updatedAt
import org.hibernate.annotations.CreationTimestamp; // For automatic timestamping
import org.hibernate.annotations.UpdateTimestamp; // For automatic timestamping

/**
 * Represents a single To-Do item in the application with extended details.
 * This class is mapped to a database table by Spring Data JPA.
 */
@Entity
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates no-arg constructor
@AllArgsConstructor // Lombok: Generates all-arg constructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description; // Added: description field
    private boolean completed;

    private LocalDate dueDate; // Added: due date field
    private String priority; // Added: priority field

    @CreationTimestamp // Automatically sets creation timestamp
    private LocalDateTime createdAt; // Added: creation timestamp

    @UpdateTimestamp // Automatically updates timestamp on modification
    private LocalDateTime updatedAt; // Added: update timestamp

    // Lombok annotations handle constructors, getters, and setters,
    // so manual ones are no longer needed.
}
