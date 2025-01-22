package com.example.travelagency.Service;

import java.util.List;

import com.example.travelagency.Entity.BusBooking;
import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;

public interface BusBookingService {
    void saveBooking(String busNumber, List<Passenger> passengers, User user);
    List<BusBooking> findBookingsByUser(User user);
     List<BusBooking> findAllBookings();
    void deleteBooking(Long id, User user);
    boolean isBookingOwnedByUser(Long id, User user);
}
