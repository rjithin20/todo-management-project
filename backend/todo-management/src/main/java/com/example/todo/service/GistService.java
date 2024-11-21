package com.example.todo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class GistService {

    @Value("${github.token}") // Fetch GitHub token from properties file
    private String githubToken;

    private static final String GITHUB_GIST_API_URL = "https://api.github.com/gists";

    public String createSecretGist(String markdownContent, String projectTitle) {
        try {
            // Prepare request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            headers.set("Content-Type", "application/json");

            // Prepare the JSON body for the Gist
            Map<String, Object> gistRequest = new HashMap<>();
            gistRequest.put("description", "Project summary for " + projectTitle);
            gistRequest.put("public", false); // Set to false for secret gist

            // Prepare the file to be included in the gist
            Map<String, Object> files = new HashMap<>();
            Map<String, Object> fileContent = new HashMap<>();
            fileContent.put("content", markdownContent);  // Include the markdown content

            files.put(projectTitle + ".md", fileContent); // File name is based on the project title

            gistRequest.put("files", files);

            // Create HttpEntity with headers and body
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(gistRequest, headers);

            // Create RestTemplate to send POST request
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    GITHUB_GIST_API_URL, HttpMethod.POST, entity, String.class);

            // Check the response from GitHub
            if (response.getStatusCode() == HttpStatus.CREATED) {
                // Parse the response JSON to extract the gist URL or ID if needed
                return "Gist created successfully!";
            } else {
                return "Error: " + response.getStatusCode() + " " + response.getBody();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating Gist: " + e.getMessage();
        }
    }
}

