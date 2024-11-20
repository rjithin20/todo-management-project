package com.example.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	List<Todo> findByProjectIdAndDeleteFlag(Long projectId, int deleteFlag);
	
	//to filter todos by status
	List<Todo> findByProjectIdAndStatusAndDeleteFlag(Long projectId, boolean status, int deleteFlag);

}
