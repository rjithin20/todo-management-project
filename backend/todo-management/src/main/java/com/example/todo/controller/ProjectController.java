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

import com.example.todo.exception.ProjectNotFoundException;
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
	@PutMapping("/title/{id}")
	public ResponseEntity<Project> updateProjectTitle(
			@PathVariable Long id, 
			@RequestBody Map<String, String> request){	
		try {
			String newTitle = request.get("title"); // title as key
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
	
	
	//generating markdown
	@PostMapping("/generate-markdown/{id}")
    public ResponseEntity<String> generateMarkdown(@PathVariable Long id) {
        try {
            // Generate markdown content
            String markdownContent = projectService.generateProjectSummaryMarkdown(id);
            
            // Fetch the project from ProjectService by id for fetching the title
            Project project = projectService.getProjectById(id)
                    .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + id + " not found"));

            // Access the project title
            String projectTitle = project.getTitle();
            
            // Save the markdown file with the project title as the filename
            projectService.saveMarkdownToFile(markdownContent, projectTitle);
            
            // Export the project summary as a secret gist
            String gistResponse = projectService.exportProjectSummaryAsGist(markdownContent, projectTitle);
            
            return new ResponseEntity<>(gistResponse, HttpStatus.OK);
       }catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
