package com.example.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.todo.model.Project;

//Repository - In Spring Data JPA, we don’t need to explicitly 
//add the @Repository annotation to the repository interfaces 
//because Spring Boot automatically detects them if they 
//extend JpaRepository or CrudRepository.

public interface ProjectRepository extends JpaRepository<Project, Long>{

	@Query("SELECT p FROM Project p WHERE p.deleteFlag = 0")
	List<Project> findAllActiveProjects();
	
	//find a project by title if required
	Project findByTitle(String title);

}
