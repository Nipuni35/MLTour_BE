package com.mlops.Services;

import java.util.List;

import com.mlops.Models.Project;

public interface ProjectService {

    List < Project > findAll() throws Exception;

}
