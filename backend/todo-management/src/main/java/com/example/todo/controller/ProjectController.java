package com.example.todo.controller;

import java.util.List;

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

import com.example.todo.model.Project;
import com.example.todo.service.ProjectService;

@RestController
@RequestMapping("/projects") //base route for all project related APIs
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	//Controller methods
	
	//create a new project
	@PostMapping
	public ResponseEntity<Project> createProject(@RequestBody Project project){
		Project savedProject = projectService.createProject(project);
		return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
	}
	
	//get all active projects
	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects(){
		List<Project> projects = projectService.getAllProjects();
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}
	
	//get a project using id
	@GetMapping("/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id){
		Project project = projectService.getProjectById(id).orElse(null);
		
		
		if (project != null) {
			return new ResponseEntity<>(project, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//update a project title
	@PutMapping("/{id}")
	public ResponseEntity<Project> updateProjectTitle(@PathVariable Long id, @RequestBody String newTitle){	
		
		//ensuring the new title isn't null or empty
		if (newTitle == null || newTitle.trim().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try {
			Project updatedProject = projectService.updateProjectTitle(id, newTitle);
			return new ResponseEntity<>(updatedProject, HttpStatus.OK);
		}catch(RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	//deleting a project
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProject(@PathVariable Long id){
		
		try {
			projectService.deleteProject(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
}
