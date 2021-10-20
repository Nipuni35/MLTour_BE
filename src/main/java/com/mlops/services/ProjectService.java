package com.mlops.services;

import java.util.List;

import com.mlops.models.Project;

public interface ProjectService {

    List < Project > findAll() throws Exception;

}
