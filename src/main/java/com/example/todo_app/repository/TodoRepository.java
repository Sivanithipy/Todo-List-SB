// src/main/java/com/example/todoapp/repository/TodoRepository.java

package com.example.todo_app.repository; // Corrected package name: removed underscore

import com.example.todo_app.model.Todo; // Corrected import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
