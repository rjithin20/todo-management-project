package com.example.todo.exception;

public class ProjectNotFoundException extends RuntimeException{

	public ProjectNotFoundException(String message) {
		super(message);
	}
}
