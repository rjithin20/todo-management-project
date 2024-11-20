package com.example.todo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Todo {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //pk
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	@JsonBackReference // Indicates this is the child reference
	private Project project;
	
	private String description;
	
	private boolean status; //true = completed, false = pending
	
	private LocalDateTime createdDate, updatedDate;
	
	private int deleteFlag = 0; //for soft deletion - 0 means active, 1 means deleted

	
	// getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
