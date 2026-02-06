package com.example.admindashboard.repository;
//DATABASE CONNECTOR

import com.example.admindashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // This simple line creates a complete SQL query behind the scenes
    Optional<User> findByUsername(String username);
}