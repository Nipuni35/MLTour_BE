package com.mlops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MlTourApplication {

    public static void main(String args[]) {
        SpringApplication.run(MlTourApplication.class, args);
    }

}
