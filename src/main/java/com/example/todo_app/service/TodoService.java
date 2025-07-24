// src/main/java/com/example/todo_app/service/TodoService.java

package com.example.todo_app.service; // Package with underscore

import com.example.todo_app.model.Todo; // Import with underscore
import com.example.todo_app.repository.TodoRepository; // Import with underscore
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service layer for managing Todo items and integrating AI features.
 * Contains business logic and interacts with the TodoRepository and external AI APIs.
 */
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // IMPORTANT: Your Gemini API Key is placed here for local development.
    // For production, use environment variables!
    private static final String GEMINI_API_KEY = "AIzaSyA7cjvRJ05XYqcXNpLwZ4DwQ0E6IsBtsvA"; // <--- YOUR API KEY IS HERE
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    @Autowired
    public TodoService(TodoRepository todoRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.todoRepository = todoRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Long id, Todo updatedTodo) {
        return todoRepository.findById(id).map(existingTodo -> {
            existingTodo.setTitle(updatedTodo.getTitle());
            existingTodo.setDescription(updatedTodo.getDescription());
            existingTodo.setCompleted(updatedTodo.isCompleted());
            existingTodo.setDueDate(updatedTodo.getDueDate());
            existingTodo.setPriority(updatedTodo.getPriority());
            return todoRepository.save(existingTodo);
        });
    }

    public boolean deleteTodo(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public String generateSubtasks(String taskDescription) {
        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            return "Please provide a task description to break down.";
        }

        String prompt = "Break down the following task into a numbered list of actionable sub-tasks. Be concise and provide only the list. Task: \"" + taskDescription + "\"";

        String requestBody = String.format(
                "{\"contents\": [{\"role\": \"user\", \"parts\": [{\"text\": \"%s\"}]}]}",
                prompt.replace("\"", "\\\"")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL + GEMINI_API_KEY, request, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode candidate = root.path("candidates").get(0);
                JsonNode content = candidate.path("content");
                JsonNode parts = content.path("parts").get(0);
                String generatedText = parts.path("text").asText();

                return generatedText;
            } else {
                return "Error generating sub-tasks: " + response.getStatusCode() + " - " + response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Exception calling Gemini API: " + e.getMessage());
            return "An error occurred while connecting to the AI service: " + e.getMessage();
        }
    }
}
