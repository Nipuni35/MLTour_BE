package com.mlops.Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.mlops.Models.Project;
import com.mlops.Services.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/projects" )
public class ProjectsController {

    @Autowired
    ProjectService projectService;

    @ApiOperation( value = "get all projects",
            response = ResponseEntity.class )
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity < List < Project > > getAllProjects(HttpServletRequest request) throws Exception {

        List < Project > projects = projectService.findAll();
        return new ResponseEntity <>(projects, HttpStatus.OK);
    }

}
