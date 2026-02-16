package com.example.admindashboard.repository;

import com.example.admindashboard.model.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.admindashboard.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository // Add this annotation
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    // Used by Employee to see their own history
    List<Timesheet> findByUserOrderByWeekStartDateDesc(User user);

    // Used by Admin to see lists of Pending/Approved/Rejected
    List<Timesheet> findByStatus(String status);

    // Add inside TimesheetRepository interface
    List<Timesheet> findByUserAndWeekStartDate(User user, LocalDate weekStartDate);

    @Query("SELECT t FROM Timesheet t WHERE t.weekStartDate BETWEEN :startDate AND :endDate " +
            "AND LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Timesheet> searchTimesheets(@Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("keyword") String keyword,
                                     Pageable pageable);

    // 2. Default fetch for Date Range only (when search is empty)
    Page<Timesheet> findByWeekStartDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);


}