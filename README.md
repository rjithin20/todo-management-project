# Todo Management Application

## Overview

This project is a Todo Management Application built using Spring Boot for the backend, MySQL for data storage, and React.js for the frontend. The application allows users to:

1. Create and manage projects.
2. Add, edit, update, and mark todos as complete within each project.
3. Export project summaries as markdown-formatted GitHub gists.
4. The application saves exported gists as markdown files locally.

## Features

- **Home Page**:
  - Create new projects.
  - List all projects.
  - View a project.
- **Project View**:
  - Editable project title.
  - List of todos with description and completion status.
  - Options to add, update, and delete todos.
  - Mark todos as pending or complete.
  - Export project summary as a secret gist.
- **Backend**:
  - Manage todos and projects.
  - Generate and export project summary in markdown format.
  - Save exported gist locally.

---

## Setup Instructions

### Step 1: Prerequisites

1. **Install MySQL** and ensure it's running.
2. **Install Node.js and npm** for the frontend.
3. **Install an IDE** such as IntelliJ IDEA, Eclipse, or Visual Studio Code to run the Spring Boot backend.

### Step 2: Backend Setup (Spring Boot)

1. **Navigate to the Backend directory**:

    ```bash
    cd backend
    ```

2. **Configure Database**:
    - Create a new MySQL database for the application, e.g., `todo_management_db`.
    - Update `src/main/resources/application.properties` with your database connection details:
    
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/todo_management_db
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    ```

3. **Generate GitHub Access Token**:
    - Go to [GitHub Token Settings](https://github.com/settings/tokens).
    - Click on "Generate new token" and select the necessary scopes (e.g., `gist`).
    - Copy the generated token and paste it in `application.properties`:

    ```properties
    github.token=your-github-access-token
    ```

4. **Run the Spring Boot Application**:
    - You can run the backend either by using the command line:

      ```bash
      ./mvnw spring-boot:run
      ```

    - Or by opening the project in an IDE such as IntelliJ IDEA, Eclipse, or Visual Studio Code and running it from there.

    - The backend will start on `http://localhost:8080`.

---

### Step 3: Frontend Setup (React.js)

1. **Navigate to the Frontend directory**:

    ```bash
    cd frontend
    ```

2. **Install Dependencies**:

    ```bash
    npm install
    ```

3. **Run the React Application**:

    ```bash
    npm start
    ```

    - The frontend will start on `http://localhost:3000`.

---

### Step 4: Testing the Application

1. **Open the Frontend** in your browser at [http://localhost:3000](http://localhost:3000).
2. **Interact with the app** by:
    - Creating a new project.
    - Managing todos within the project (add, edit, update, and mark as complete).
    - Exporting the project summary as a GitHub gist.

---

## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, MySQL
- **Frontend**: React.js, Axios
- **Other**: GitHub Gist API for exporting summaries

---

## Future Improvements

- Implement authentication (Basic Auth or JWT).
- Add unit and integration tests.
- Improve UI/UX for a better user experience.

---

