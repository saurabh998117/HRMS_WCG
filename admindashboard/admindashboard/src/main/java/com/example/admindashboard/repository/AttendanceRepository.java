package com.example.admindashboard.repository;

import com.example.admindashboard.model.Attendance;
import com.example.admindashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Find today's attendance for a specific user (to prevent double check-in)
    Optional<Attendance> findByUserAndDate(User user, LocalDate date);

    // Find all attendance for a user (for history)
    List<Attendance> findByUserOrderByDateDesc(User user);
    long countByDate(LocalDate date);
}