package com.example.admindashboard.repository;

import com.example.admindashboard.model.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    // Standard CRUD operations are inherited automatically
}