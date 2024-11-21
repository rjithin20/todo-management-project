import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { API_ROUTES } from "../../apiRoutes";
import "./ProjectDetails.css";


const ProjectDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [todos, setTodos] = useState([]);
  const [projectTitle, setProjectTitle] = useState("");
  const [newTodo, setNewTodo] = useState("");
  const [error, setError] = useState("");
  const [editTodoId, setEditTodoId] = useState(null);
  const [editDescription, setEditDescription] = useState("");

  const [editProjectTitle, setEditProjectTitle] = useState(false); // State to track project title edit mode
  const [updatedTitle, setUpdatedTitle] = useState(projectTitle); // State for updated title

  useEffect(() => {
    fetchProjectDetails();
    fetchTodos();
  }, [id]); //id - project id

  const fetchProjectDetails = async () => {
    try {
      const response = await axios.get(`${API_ROUTES.GET_PROJECT_BY_ID}/${id}`);
      setProjectTitle(response.data.title);
      setError("");
    } catch (err) {
      setError("Error fetching project details");
      console.error(err);
    }
  };

  const handleUpdateProjectTitle = async () => {
    try {
      const response = await axios.put(
        `${API_ROUTES.UPDATE_PROJECT_TITLE}/${id}`,
        {
          title: updatedTitle,
        }
      );
      setProjectTitle(response.data.title);
      setEditProjectTitle(false); // Exit edit mode
      setError("");
    } catch (err) {
      setError("Error updating project title");
      console.error(err);
    }
  };

  const fetchTodos = async () => {
    try {
      const response = await axios.get(`${API_ROUTES.GET_ALL_TODOS}/${id}`);
      setTodos(response.data);
      setError("");
    } catch (err) {
      setError("Error fetching todos");
      console.error(err);
    }
  };

  const handleCreateTodo = async () => {
    if (!newTodo.trim()) {
      setError("Todo description cannot be empty");
      return;
    }
    try {
      const response = await axios.post(`${API_ROUTES.CREATE_TODO}/${id}`, {
        description: newTodo,
      });
      setTodos((prevTodos) => [...prevTodos, response.data]);
      setNewTodo("");
      setError("");
    } catch (err) {
      setError("Error creating todo");
      console.error(err);
    }
  };

  const handleDeleteTodo = async (todoId) => {
    try {
      await axios.delete(`${API_ROUTES.DELETE_TODO}/${todoId}`);
      setTodos((prevTodos) => prevTodos.filter((todo) => todo.id !== todoId));
      setError("");
    } catch (err) {
      setError("Error deleting todo");
      console.error(err);
    }
  };

  const handleEditClick = (todo) => {
    setEditTodoId(todo.id);
    setEditDescription(todo.description);
  };

  const handleUpdateTodoDescription = async (todoId) => {
    try {
      const response = await axios.put(
        `${API_ROUTES.UPDATE_TODO_DESC}/${todoId}`,
        {
          description: editDescription,
        }
      );
      setTodos((prevTodos) =>
        prevTodos.map((todo) =>
          todo.id === todoId ? { ...todo, ...response.data } : todo
        )
      );
      setEditTodoId(null);
      setEditDescription("");
      setError("");
    } catch (err) {
      setError("Error updating todo");
      console.error(err);
    }
  };

  const handleStatusChange = async (todoId, newStatus) => {
    try {
      const response = await axios.put(
        `${API_ROUTES.UPDATE_TODO_STATUS}/${todoId}`,
        {
          status: newStatus,
        }
      );
      setTodos((prevTodos) =>
        prevTodos.map((todo) =>
          todo.id === todoId ? { ...todo, status: response.data.status } : todo
        )
      );
      setError("");
    } catch (err) {
      setError("Error updating status");
      console.error(err);
    }
  };

  const handleExportGist = async () => {
    try {
      const response = await axios.post(
        `${API_ROUTES.EXPORT_GIST}/${id}`
      );
      alert("Project summary has been successfully exported as a Gist! and stored in the local system");
      console.log(response.data);
    } catch (err) {
      setError("Error exporting project as Gist");
      console.error(err);
    }
  };

  return (
    <div className="project-details-container">

      {/* change project title when needed */}
      <h1>
        {editProjectTitle ? (
          <input
            type="text"
            value={updatedTitle}
            onChange={(e) => setUpdatedTitle(e.target.value)}
            placeholder={'current title : '+projectTitle}
          />
        ) : (
          projectTitle //display current title if not in edit mode
        )}
      </h1>

      {/* button to toggle project title edit mode  */}
      <button
        className="edit-btn"
        onClick={() => setEditProjectTitle(!editProjectTitle)} //toggle edit mode
      >
        {editProjectTitle ? "Cancel" : "Change Project Title"}
      </button>

      {/* Update and Cancel buttons if in edit mode */}
      {editProjectTitle && (
        <button className="update-btn" onClick={handleUpdateProjectTitle}>Update</button>
      )}

      <button className="back-btn" onClick={() => navigate("/")}>
        Back to Projects
      </button>

      
      {/* Export Gist Button */}
      <button onClick={handleExportGist} className="export-gist-btn">
        Export as Gist
      </button>

      {/* creating todos */}
      <div className="create-todo">
        <input
          type="text"
          id="todo-desc"
          value={newTodo}
          onChange={(e) => setNewTodo(e.target.value)}
          placeholder="Enter todo description"
        />
        <button onClick={handleCreateTodo}>Add Todo</button>
      </div>

      {error && <p className="error-message">{error}</p>}

      {/* display todos */}
      <div className="todos-table">
        <h2>Todos</h2>
        <table>
          <thead>
            <tr>
              <th>No.</th>
              <th>Description</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {todos.map((todo, index) => (
              <tr key={todo.id}>
                <td>{index + 1}</td>
                <td>
                  {editTodoId === todo.id ? (
                    <input
                      type="text"
                      value={editDescription}
                      onChange={(e) => setEditDescription(e.target.value)}
                    />
                  ) : (
                    todo.description
                  )}
                </td>
                <td>{todo.status ? "Completed" : "Pending"}</td>
                <td>
                  {editTodoId === todo.id ? (
                    <>
                      <button
                        className="update-btn"
                        onClick={() => handleUpdateTodoDescription(todo.id)}
                      >
                        Update
                      </button>
                      <button
                        className="cancel-btn"
                        onClick={() => setEditTodoId(null)}
                      >
                        Cancel
                      </button>
                    </>
                  ) : (
                    <>
                      <button
                        className="edit-btn"
                        onClick={() => handleEditClick(todo)}
                      >
                        Edit
                      </button>
                      <button
                        className="delete-btn"
                        onClick={() => handleDeleteTodo(todo.id)}
                      >
                        Delete
                      </button>
                      <input
                        type="checkbox"
                        className="status-checkbox"
                        checked={todo.status}
                        onChange={() =>
                          handleStatusChange(todo.id, !todo.status)
                        }
                      />
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ProjectDetails;
