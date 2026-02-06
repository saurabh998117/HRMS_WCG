package com.example.admindashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// FORCE Spring to find your Database files
@EntityScan(basePackages = "com.example.admindashboard.model")
@EnableJpaRepositories(basePackages = "com.example.admindashboard.repository")
public class AdmindashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmindashboardApplication.class, args);
    }
}