package com.example.admindashboard.service;
//LINk between USER and DATABASE CONNECTOR

import com.example.admindashboard.model.User;
import com.example.admindashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DEBUG LINE: Print to console
        System.out.println("Attempting login for user: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User NOT found: " + username); // Debug failure
                    return new UsernameNotFoundException("User not found");
                });

        System.out.println("User FOUND! Password is: " + user.getPassword()); // Debug success

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }

}