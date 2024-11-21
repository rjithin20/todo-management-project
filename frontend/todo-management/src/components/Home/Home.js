import React, { useState, useEffect } from "react";
import axios from "axios";
import { API_ROUTES } from "../../apiRoutes";
import { Link } from "react-router-dom";
import "./Home.css";

const Home = () => {
  // State to hold the project data and form input
  const [projects, setProjects] = useState([]);
  const [newProjectTitle, setNewProjectTitle] = useState("");
  const [error, setError] = useState("");

  // Fetch all projects from the backend
  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const response = await axios.get(API_ROUTES.GET_ALL_PROJECTS);
      // Sort projects by creation date (ascending order)
      const sortedProjects = response.data.sort(
        (a, b) => new Date(a.createdDate) - new Date(b.createdDate)
      );
      setProjects(sortedProjects);
      setError("");
    } catch (err) {
      setError("Error fetching projects");
      console.error("Fetch Projects Error:", err);
    }
  };

  // Handle the project creation
  const handleCreateProject = async () => {
    if (!newProjectTitle.trim()) {
      setError("Project title cannot be empty");
      return;
    }

    try {
      const response = await axios.post(API_ROUTES.CREATE_PROJECT, {
        title: newProjectTitle,
      });
      const newProject = response.data;
      setProjects((prevProjects) =>
        [...prevProjects, newProject].sort(
          (a, b) => new Date(a.createdDate) - new Date(b.createdDate)
        )
      );
      setNewProjectTitle("");
      setError("");
    } catch (err) {
      setError("Error creating project");
      console.error("Create Project Error:", err);
    }
  };

  // Handle the project deletion
  const handleDeleteProject = async (id) => {
    try {
      // console.log(id);
      const url = `${API_ROUTES.DELETE_PROJECT}/${id}`; // Ensure proper string interpolation
      await axios.delete(url);
      setProjects((prevProjects) =>
        prevProjects.filter((project) => project.id !== id)
      );
      setError("");
    } catch (err) {
      setError("Error deleting project");
      console.error("Delete Project Error:", err);
    }
  };

  return (
    <div className="home-container">
      <h1>Todo Projects</h1>

      <div className="create-project">
        <input
          type="text"
          value={newProjectTitle}
          onChange={(e) => setNewProjectTitle(e.target.value)}
          placeholder="Enter project title"
        />
        <button onClick={handleCreateProject}>Create Project</button>
      </div>

      {error && <p className="error-message">{error}</p>}

      <div className="projects-table">
        <h2>All Projects</h2>
        <table>
          <thead>
            <tr>
              <th>No.</th>
              <th>Title</th>
              <th>Created Date & Time</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {projects.map((project, index) => (
              <tr key={project.id}>
                <td>
                  {index + 1}
                </td>
                <td>
                  <Link to={`/projects/${project.id}`}>{project.title}</Link>
                </td>
                <td>{new Date(project.createdDate).toLocaleString()}</td>
                <td>
                  <button
                    className="delete-btn"
                    onClick={() => handleDeleteProject(project.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Home;
