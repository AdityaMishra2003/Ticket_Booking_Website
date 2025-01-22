package com.example.travelagency.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelagency.Entity.HotelBooking;
import com.example.travelagency.Entity.User;

import java.util.List;
import java.util.Optional;


public interface HotelBookingRepository extends JpaRepository<HotelBooking,Long>{
    List<HotelBooking> findByUser(User user);
     Optional<HotelBooking> findByIdAndUser(Long id, User user);
}
