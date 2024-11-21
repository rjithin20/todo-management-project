package com.example.todo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.exception.ProjectNotFoundException;
import com.example.todo.model.Project;
import com.example.todo.model.Todo;
import com.example.todo.repository.ProjectRepository;
import com.example.todo.repository.TodoRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private GistService gistService;
	
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
			throw new ProjectNotFoundException("Project with ID " + id + " not found");
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
	        throw new ProjectNotFoundException("Project with ID " + id + " not found");
	    }
	}
	
	
	//method to generate the project summary in markdown format
	public String generateProjectSummaryMarkdown(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (!projectOpt.isPresent()) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
        }

        Project project = projectOpt.get();
        List<Todo> todos = todoRepository.findByProjectIdAndDeleteFlag(projectId, 0);

        // Count completed and pending todos
        long completedCount = todos.stream().filter(Todo::isStatus).count();
        long totalCount = todos.size();

        // Build Markdown content
        StringBuilder markdown = new StringBuilder();
        markdown.append("# ").append(project.getTitle()).append("\n\n");
        markdown.append("## Summary\n");
        markdown.append(completedCount).append(" / ").append(totalCount).append(" completed.\n\n");

        // Section 1: Pending Todos (open checkboxes)
        markdown.append("## Pending Todos\n");
        todos.stream()
              .filter(todo -> !todo.isStatus())
              .forEach(todo -> markdown.append("- [ ] ").append(todo.getDescription()).append("\n"));
        markdown.append("\n");

        // Section 2: Completed Todos (checked checkboxes)
        markdown.append("## Completed Todos\n");
        todos.stream()
              .filter(Todo::isStatus)
              .forEach(todo -> markdown.append("- [x] ").append(todo.getDescription()).append("\n"));

        return markdown.toString();
    }
	
	// save the file in the local system
	public void saveMarkdownToFile(String markdownContent, String projectTitle) {
	    // Ensure the project title is a valid file name (remove any invalid characters)
	    String sanitizedProjectTitle = projectTitle.replaceAll("[^a-zA-Z0-9\\-_]", "_");

	    // Construct the file name using the sanitized project title
	    String fileName = sanitizedProjectTitle + ".md";

	    try {
	        Path filePath = Paths.get("C:\\Users\\rjith\\Documents\\Project-summaries", fileName); // Adjust the path as needed
	        Files.write(filePath, markdownContent.getBytes());
	        System.out.println("Markdown saved to " + filePath.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error saving Markdown file: " + e.getMessage());
	    }
	}
	
	// Method to export the project summary as a secret gist
    public String exportProjectSummaryAsGist(String markdownContent, String projectTitle) {
        return gistService.createSecretGist(markdownContent, projectTitle);
    }

}
