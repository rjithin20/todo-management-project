package com.example.todo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.model.Project;
import com.example.todo.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	//method for creating a new project
	public Project createProject(Project project) {
		project.setCreatedDate(LocalDateTime.now());
		return projectRepository.save(project);
	}
	
	// method for fecthing the active projects - delete flag = 0
	public List<Project> getAllProjects(){
		return projectRepository.findAllActiveProjects();
	}
	
	//fetch the project using id which is active
	public Optional<Project> getProjectById(Long id){
		Optional<Project> project = projectRepository.findById(id);
		
		if(project.isPresent() && project.get().getDeleteFlag() == 0) {
			return project;
		}else {
			return Optional.empty();
		}
	}
	
	//using the getProjectById() for fetching the active project and updating the title
	public Project updateProjectTitle(Long id, String newTitle) {
		Optional<Project> optionalProject = getProjectById(id);
		
		if(optionalProject.isPresent()) {
			//update the title of the active project
			Project project = optionalProject.get();
			project.setTitle(newTitle);
			//save and return the updated project
			return projectRepository.save(project);
		}else {
			throw new RuntimeException("project not found or deleted");
		}
	}
	
	//using the getProjectById() for fetching the active project and soft delete the project
	public void deleteProject(Long id) {
		Optional<Project> optionalProject = getProjectById(id);
		
		if (optionalProject.isPresent()) {
	        // delete the project 
	        Project project = optionalProject.get();
	        project.setDeleteFlag(1); // Mark as deleted
	        projectRepository.save(project); // Save the updated project
	    } else {
	        throw new RuntimeException("Project not found or already deleted");
	    }
	}
}
