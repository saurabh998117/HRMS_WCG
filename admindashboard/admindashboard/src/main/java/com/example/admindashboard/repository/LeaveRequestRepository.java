package com.example.admindashboard.repository;

import com.example.admindashboard.model.LeaveRequest;
import com.example.admindashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUserOrderByFromDateDesc(User user);
    List<LeaveRequest> findByStatus(String status);
}