package com.mlops.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import org.dom4j.tree.AbstractEntity;

@Entity
@Getter
@Setter
public class ProjectModel extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -6316972225029499319L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String name;
    private String gitUrl;

}
