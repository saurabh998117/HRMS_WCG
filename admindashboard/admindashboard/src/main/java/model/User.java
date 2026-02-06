package com.example.admindashboard.model;
// User PROFILE

import jakarta.persistence.*;

@Entity
@Table(name = "users") // This connects to the 'users' table in Postgres
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Stores ADM001, EMP001, etc.

    @Column(nullable = false)
    private String password;

    private String role; // Stores ROLE_ADMIN, ROLE_USER

    // --- CONSTRUCTORS (Needed by Spring) ---
    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}