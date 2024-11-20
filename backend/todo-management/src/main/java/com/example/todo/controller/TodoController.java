package com.example.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("/todos") //base route for all Todo-related APIs
public class TodoController {

	@Autowired
	private TodoService todoService;
	
	//create a new todo for a project
	@PostMapping("/projects/{projectId}")
	public ResponseEntity<Todo> createTodo(@PathVariable Long projectId, @RequestBody Todo todo){
		try {
			Todo createdTodo = todoService.createTodoInProject(projectId, todo);
			return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
		}catch(RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//get all active todos of a project
	@GetMapping("/projects/{projectId}")
	public ResponseEntity<List<Todo>> getAllTodos(@PathVariable Long projectId){
		List<Todo> todos = todoService.getAllTodos(projectId);
		return new ResponseEntity<List<Todo>>(todos, HttpStatus.OK);
	}
	
	//update a todo(description, status)
	@PutMapping("/{todoId}")
	public ResponseEntity<Todo> updateTodo(@PathVariable Long todoId, @RequestBody Todo todo){
		try {
			Todo updateTodo = todoService.updateTodo(todoId, todo.getDescription(), todo.isStatus());
			return new ResponseEntity<>(updateTodo, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//delete a todo
	@DeleteMapping("/{todoId}")
	public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId){
		try {
			todoService.deleteTodo(todoId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//display all the todos with project name - active projects
	@GetMapping("/grouped-by-project")
    public ResponseEntity<Map<String, List<Todo>>> getTodosGroupedByProjectName() {
        Map<String, List<Todo>> groupedTodos = todoService.getTodosGroupedByProjectName();
        return new ResponseEntity<>(groupedTodos, HttpStatus.OK);
    }
}










