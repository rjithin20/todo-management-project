const BASE_URL = "http://localhost:8080"; // Base URL to connect backend

// APIs and methods  => id's appended dynamically
export const API_ROUTES = {
  // Project APIs

  CREATE_PROJECT: `${BASE_URL}/projects`, // POST

  GET_ALL_PROJECTS: `${BASE_URL}/projects`, // GET

  GET_PROJECT_BY_ID: `${BASE_URL}/projects`, // GET

  UPDATE_PROJECT_TITLE: `${BASE_URL}/projects/title`, // PUT

  DELETE_PROJECT: `${BASE_URL}/projects`, // DELETE 

  // Todo APIs
  CREATE_TODO: `${BASE_URL}/todos/projects`, // POST

  GET_ALL_TODOS: `${BASE_URL}/todos/projects`, // GET

  UPDATE_TODO_DESC: `${BASE_URL}/todos/desc`, // PUT

  UPDATE_TODO_STATUS: `${BASE_URL}/todos/status`, // PUT

  DELETE_TODO: `${BASE_URL}/todos`, // DELETE

  // TODOS_GROUPED_BY_PROJECT: `${BASE_URL}/todos/grouped-by-project`, // GET - not required

  // Gist APIs
  EXPORT_GIST: `${BASE_URL}/projects/generate-markdown` // POST

};

