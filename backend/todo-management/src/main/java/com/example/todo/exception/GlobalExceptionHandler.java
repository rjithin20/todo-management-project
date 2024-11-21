package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	//handle ProjectNotFoundException
	@ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException exception){
		return new ResponseEntity<>("Project Error: " + exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	// handle TodoNotFoundException
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<String> handleTodoNotFoundException(TodoNotFoundException exception) {
        return new ResponseEntity<>("Todo Error: " + exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    // generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception exception) {
        return new ResponseEntity<>("An error occurred: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
