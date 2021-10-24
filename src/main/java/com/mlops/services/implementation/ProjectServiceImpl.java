package com.mlops.services.implementation;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlops.models.Project;
import com.mlops.models.ProjectModel;
import com.mlops.repositories.ProjectRepository;
import com.mlops.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import hudson.util.Secret;
//import org.apache.http.HttpEntity;

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

        return project;
    }

    @Override
    public String createProject(String name) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        Map < String, Object > repositoryPayLoad = new HashMap <>();
        repositoryPayLoad.put("name", name);
        repositoryPayLoad.put("private", true);
        String url = "https://api.github.com/user/repos";
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

}
