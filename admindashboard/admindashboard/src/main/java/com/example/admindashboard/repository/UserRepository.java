package com.example.admindashboard.repository;
//DATABASE CONNECTOR

import com.example.admindashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // This simple line creates a complete SQL query behind the scenes
    Optional<User> findByUsername(String username);

    // 1. Find ALL Employees (for the default list)
    List<User> findByRoleOrderByUsernameAsc(String role);

    // 2. SEARCH only within Employees (Custom Query)
    // "Select users where Role is X AND (Name matches OR ID matches)"
    @Query("SELECT u FROM User u WHERE u.role = :role AND " +
            "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY u.username ASC")
    List<User> searchByRole(@Param("role") String role, @Param("search") String search);

    // Add this inside the interface
    long countByRole(String role);
}


