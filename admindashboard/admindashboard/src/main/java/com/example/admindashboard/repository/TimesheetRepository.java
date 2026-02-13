package com.example.admindashboard.repository;

import com.example.admindashboard.model.Timesheet;
import com.example.admindashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Import this
import java.util.List;

@Repository // Add this annotation
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    // Used by Employee to see their own history
    List<Timesheet> findByUserOrderByWeekStartDateDesc(User user);

    // Used by Admin to see lists of Pending/Approved/Rejected
    List<Timesheet> findByStatus(String status);
}