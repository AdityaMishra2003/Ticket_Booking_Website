package com.example.travelagency.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelagency.Entity.BusBooking;
import com.example.travelagency.Entity.User;

public interface BusBookingRepository extends JpaRepository<BusBooking, Long> {
    List<BusBooking> findByUser(User user);
    Optional<BusBooking> findByIdAndUser(Long id, User user);
}
