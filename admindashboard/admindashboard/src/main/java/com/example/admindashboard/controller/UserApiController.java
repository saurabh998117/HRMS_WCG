package com.example.admindashboard.controller;

import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    // The endpoint our frontend JavaScript is trying to fetch
    @GetMapping("/api/users/search")
    public ResponseEntity<List<User>> searchEmployees(@RequestParam("query") String query) {

        // 1. We will call a custom method in your UserRepository (which we'll build next)
        // 2. It will look for the query string inside the employee's name, ID, or department.
        List<User> matchingUsers = userRepository.searchByKeyword(query);

        // 3. Return the list of users as JSON back to the frontend
        return ResponseEntity.ok(matchingUsers);
    }

    @GetMapping("/api/users/birthdays-today")
    public ResponseEntity<List<User>> getBirthdaysToday() {
        List<User> users = userRepository.findByBirthdayToday();
        return ResponseEntity.ok(users);
    }
}