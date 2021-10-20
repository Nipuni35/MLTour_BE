package com.mlops.services.implementation;

import java.util.ArrayList;
import java.util.List;

import com.mlops.models.Project;
import com.mlops.models.ProjectModel;
import com.mlops.repositories.ProjectRepository;
import com.mlops.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
