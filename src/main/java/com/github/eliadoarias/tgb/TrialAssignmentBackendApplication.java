package com.github.eliadoarias.tgb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.eliadoarias.tgb.mapper")
public class TrialAssignmentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrialAssignmentBackendApplication.class, args);
    }

}
