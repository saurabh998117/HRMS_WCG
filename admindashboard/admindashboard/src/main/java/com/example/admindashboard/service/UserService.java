package com.example.admindashboard.service;

import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional
    public void updateEmployeeProfessionalDetails(Long id, User updatedData) {
        User existingUser = findById(id);

        // Safely update only the allowed professional details
        existingUser.setDesignation(updatedData.getDesignation());
        existingUser.setBusinessUnit(updatedData.getBusinessUnit()); // Matching your HTML variable
        existingUser.setProjectName(updatedData.getProjectName());   // Matching your HTML variable
        existingUser.setReportingManager(updatedData.getReportingManager());
        existingUser.setProjectCode(updatedData.getProjectCode());
        existingUser.setBuHrContact(updatedData.getBuHrContact());
        existingUser.setTeamGroup(updatedData.getTeamGroup());
        existingUser.setProjectManager(updatedData.getProjectManager());

        userRepository.save(existingUser);
    }
}