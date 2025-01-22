package com.example.travelagency.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.travelagency.Entity.TrainBooking;
import com.example.travelagency.Entity.User;

public interface TrainBookingRepository extends JpaRepository<TrainBooking,Long>{
    List<TrainBooking> findByUser(User user);
     Optional<TrainBooking> findByIdAndUser(Long id, User user);
}
