package com.mlops.repositories;

import java.util.List;

import com.mlops.models.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository < ProjectModel, Long > {

    List < ProjectModel > findAll();

}
