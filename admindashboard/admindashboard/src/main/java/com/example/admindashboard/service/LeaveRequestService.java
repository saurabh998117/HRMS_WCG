package com.example.admindashboard.service; // Adjust package name if needed

import com.example.admindashboard.model.LeaveRequest;
import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.LeaveRequestRepository;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch the logged-in user's leave history
    public List<LeaveRequest> getMyLeaves(String username) {

        // 1. Fetch the actual User entity from the database using their username/ID
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // 2. Pass that specific User object to your updated repository method
        return leaveRequestRepository.findByUserOrderByIdDesc(currentUser);
    }
}