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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found with username: " + username));
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

    @Transactional
    public void updateEmployeePersonalDetails(String username, User updatedData) {
        // Find the logged-in user securely using their username
        User existingUser = findByUsername(username);

        // ONLY update personal fields allowed by Admin policy
        existingUser.setMobileNumber(updatedData.getMobileNumber());
        existingUser.setWorkLocation(updatedData.getWorkLocation());
        existingUser.setCity(updatedData.getCity());

        // Save the changes
        userRepository.save(existingUser);
    }
}