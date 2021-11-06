package com.mlops.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.mlops.models.Project;
import com.mlops.services.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping( "/projects" )
public class ProjectsController {

    @Autowired
    private ProjectService projectService;

    @ApiOperation( value = "get all projects",
            response = ResponseEntity.class )
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
    @CrossOrigin( origins = "http://localhost:4200" )
    public ResponseEntity < List < Project > > getAllProjects(HttpServletRequest request) throws Exception {

        List < Project > projects = projectService.findAll();
        return new ResponseEntity <>(projects, HttpStatus.OK);
    }

    @ApiOperation( value = "get project by Id",
            response = ResponseEntity.class )
    @GetMapping( path = "/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE )
    @CrossOrigin( origins = "http://localhost:4200" )
    public ResponseEntity < Project > getProject(
            @PathVariable( name = "projectId" )
                    Long projectId, HttpServletRequest request) throws Exception {

        Project project = projectService.findById(projectId);
        return new ResponseEntity <>(project, HttpStatus.OK);
    }

    @ApiOperation( value = "create project",
            response = ResponseEntity.class )
    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE )
    @CrossOrigin( origins = "http://localhost:4200" )
    public ResponseEntity < String > createProject(
            @RequestParam
                    String projectName,
            @RequestParam
                    String apiServingTool,
            MultipartHttpServletRequest multipartHttpServletRequest,
            HttpServletRequest request) throws Exception {

        String gitUrl = projectService.createProject(projectName, apiServingTool, multipartHttpServletRequest);
        return new ResponseEntity <>(gitUrl, HttpStatus.OK);
    }

}
