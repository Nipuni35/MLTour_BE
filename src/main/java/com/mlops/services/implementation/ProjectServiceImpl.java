package com.mlops.services.implementation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlops.models.Project;
import com.mlops.models.ProjectModel;
import com.mlops.repositories.ProjectRepository;
import com.mlops.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List < Project > findAll() throws Exception {
        List < Project > projects = new ArrayList <>();
        List < ProjectModel > projectList = projectRepository.findAll();
        for ( ProjectModel projectModel :
                projectList ) {
            Project project = new Project();
            project.setId(projectModel.getId());
            project.setName(projectModel.getName());
            project.setGitUrl(projectModel.getGitUrl());
            projects.add(project);
        }
        return projects;
    }

    @Override
    public Project findById(Long projectId) throws Exception {
        Optional < ProjectModel > optionalProjectModel = projectRepository.findById(projectId);
        if ( !optionalProjectModel.isPresent() ) {
            throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST));
        }
        Project project = new Project();
        project.setId(optionalProjectModel.get().getId());
        project.setName(optionalProjectModel.get().getName());
        project.setGitUrl(optionalProjectModel.get().getGitUrl());

        return project;
    }

    @Override
    public String createProject(String projectName,
                                String apiServingTool, MultipartHttpServletRequest request) throws Exception {
        String response = createRepository(projectName);
        Iterator < String > itr = request.getFileNames();
        MultipartFile file = null;

        while ( itr.hasNext() ) {
            String element = itr.next();
            file = request.getFile(element);
            String shaOfFile = null;
            byte[] fileContent = Objects.requireNonNull(request.getFile(element)).getBytes();
            if ( element.equalsIgnoreCase("apiServingFile") ) {
                shaOfFile = getFileDetails(projectName, fileContent, file);
            }
            uploadFiles(projectName, fileContent, file, shaOfFile);
        }
        ProjectModel projectModel = new ProjectModel();
        projectModel.setName(projectName);
        projectModel.setGitUrl(response);
        projectRepository.save(projectModel);
        return projectModel.toString();
    }

    private String createRepository(String projectName) throws URISyntaxException, java.io.IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        Map < String, Object > repositoryPayLoad = new HashMap <>();
        repositoryPayLoad.put("name", projectName);
        repositoryPayLoad.put("private", true);
        String url = "https://api.github.com/repos/Nipuni35/flask-template/generate";
        URI uri = new URI(url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "token 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");

        HttpEntity < Object > request = new HttpEntity <>(repositoryPayLoad, headers);
        String response = restTemplate.postForObject(uri, request, String.class);
        if ( response != null ) {
            Map < String, Object > map = mapper.readValue(response, Map.class);
            response = map.get("git_url").toString();
        }
        return response;
    }

    private void uploadFiles(String projectName, byte[] content, MultipartFile file, String sha) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        Map < String, Object > repositoryPayLoad = new HashMap <>();
        repositoryPayLoad.put("content", encodeFileToBase64(content));
        repositoryPayLoad.put("message", "initial commit");
        if ( sha != null ) {
            repositoryPayLoad.put("sha", sha);
        }
        String url = "https://api.github.com/repos/Nipuni35/" + projectName + "/contents/" + file.getOriginalFilename();
        URI uri = new URI(url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "token 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");

        HttpEntity < Object > request = new HttpEntity <>(repositoryPayLoad, headers);
        restTemplate.put(uri, request);
    }

    private String getFileDetails(String projectName, byte[] content, MultipartFile file) throws URISyntaxException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        Map < String, Object > repositoryPayLoad = new HashMap <>();
        ObjectMapper mapper = new ObjectMapper();
        repositoryPayLoad.put("message", "initial commit");
        String url = "https://api.github.com/repos/Nipuni35/" + projectName + "/contents/app.py";
        URI uri = new URI(url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "token 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");

        HttpEntity < Object > request = new HttpEntity <>(repositoryPayLoad, headers);
        ResponseEntity < String > response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        String sha = null;
        if ( response != null ) {
            Map < String, Object > map = mapper.readValue(response.getBody(), Map.class);
            sha = map.get("sha").toString();
        }
        return sha;
    }

    private static String encodeFileToBase64(byte[] fileContent) {
        return Base64.getEncoder().encodeToString(fileContent);
    }

}
