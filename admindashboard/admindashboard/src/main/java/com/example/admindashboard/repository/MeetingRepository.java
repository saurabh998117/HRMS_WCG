package com.example.admindashboard.repository;

import com.example.admindashboard.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    // Fetches all meetings scheduled for today or in the future, sorted by date and time
    List<Meeting> findByMeetingDateGreaterThanEqualOrderByMeetingDateAscStartTimeAsc(LocalDate date);
}