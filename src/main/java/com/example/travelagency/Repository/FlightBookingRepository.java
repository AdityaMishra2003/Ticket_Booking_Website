package com.example.travelagency.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelagency.Entity.FlightBooking;
import com.example.travelagency.Entity.User;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    List<FlightBooking> findByUser(User user);
     Optional<FlightBooking> findByIdAndUser(Long id, User user);
}
