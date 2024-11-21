package com.example.todo.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.exception.ProjectNotFoundException;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.model.Project;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private ProjectService projectService;
	
	//create  a new todo within a project
	public Todo createTodoInProject(Long projectId, Todo todo) {
		Optional<Project> project = projectService.getProjectById(projectId);
		
		if(project.isPresent()) {
			todo.setCreatedDate(LocalDateTime.now());
			todo.setProject(project.get());
			return todoRepository.save(todo);
		}else {
			throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
		}
	}
	
	//get all active todos for a Project
	public List<Todo> getAllTodos(Long projectId){
		return todoRepository.findByProjectIdAndDeleteFlag(projectId, 0);
	}
	
	//update a todo's description
	public Todo updateTodoDescription(Long todoId, String description) {
		Optional<Todo> todo = todoRepository.findById(todoId);
		
		if(todo.isPresent()) {
			Todo existingTodo = todo.get();
			existingTodo.setDescription(description);
			existingTodo.setUpdatedDate(LocalDateTime.now());
			return todoRepository.save(existingTodo);
		}else {
			throw new TodoNotFoundException("Todo with ID " + todoId + " not found");
		}
	}
	
	//update a todo's status
	public Todo updateTodoStatus(Long todoId, boolean isCompleted) {
		Optional<Todo> todo = todoRepository.findById(todoId);
		
		if(todo.isPresent()) {
			Todo existingTodo = todo.get();
			existingTodo.setStatus(isCompleted);
			existingTodo.setUpdatedDate(LocalDateTime.now());
			return todoRepository.save(existingTodo);
		}else {
			throw new TodoNotFoundException("Todo with ID " + todoId + " not found");
		}
	}
	
	//delete a todo
	public void deleteTodo(Long  todoId) {
		Optional<Todo> todo = todoRepository.findById(todoId);
		if(todo.isPresent()) {
			Todo existingTodo = todo.get();
			existingTodo.setDeleteFlag(1); //mark as deleted
			todoRepository.save(existingTodo);
		}else {
			throw new TodoNotFoundException("Todo with ID " + todoId + " not found");
		}
	}
	
//	//for displaying all the todos with their project name
//	public Map<String, List<Todo>> getTodosGroupedByProjectName() {
//        List<Todo> todos = todoRepository.findAll(); // Fetch all todos
//        Map<String, List<Todo>> groupedTodos = new HashMap<>();
//
//        for (Todo todo : todos) {
//            if (todo.getDeleteFlag() == 0) { // Only include active todos
//                String projectName = todo.getProject().getTitle();
//
//                // Check if the project name is already a key in the map
//                if (!groupedTodos.containsKey(projectName)) {
//                    groupedTodos.put(projectName, new ArrayList<>());
//                }
//
//                // Add the todo to the project's list
//                groupedTodos.get(projectName).add(todo);
//            }
//        }
//
//        return groupedTodos;
//    }
	
}
