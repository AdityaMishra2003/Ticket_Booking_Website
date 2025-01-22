package com.example.travelagency.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.TrainBooking;
import com.example.travelagency.Entity.User;

public interface TrainBookingService {
    void saveBooking(String trainnumber,String trainname,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, List<Passenger> passengers, User user);
    List<TrainBooking> findBookingsByUser(User user);
    List<TrainBooking> findAllBookings();
    void deleteTrainBooking(Long id, User user);
    boolean isBookingOwnedByUser(Long id, User user);

}
