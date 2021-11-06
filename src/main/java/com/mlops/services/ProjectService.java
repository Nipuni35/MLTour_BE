package com.mlops.services;

import java.util.List;

import com.mlops.models.Project;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ProjectService {

    List < Project > findAll() throws Exception;

    Project findById(Long projectId) throws Exception;

    String createProject(String projectName,
                         String apiServingTool, MultipartHttpServletRequest request) throws Exception;

}
