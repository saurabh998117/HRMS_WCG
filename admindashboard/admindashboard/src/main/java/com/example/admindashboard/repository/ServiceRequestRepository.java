package com.whitecircle.hrms.repository;

import com.whitecircle.hrms.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByEmployeeIdOrderBySubmissionDateDesc(String employeeId);
    List<ServiceRequest> findByStatus(String status);
}