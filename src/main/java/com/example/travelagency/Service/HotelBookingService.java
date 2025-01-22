package com.example.travelagency.Service;

import java.time.LocalDate;
import java.util.List;

import com.example.travelagency.Entity.HotelBooking;
import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;

public interface HotelBookingService {
    void saveBooking(String hotelname, List<Passenger> passengers, String cityname, LocalDate checkIn, LocalDate checkOut, User user);
    List<HotelBooking> findBookingsByUser(User user);
    List<HotelBooking> findAllBookings();
    void deleteHotelBooking(Long id, User user);
    boolean isBookingOwnedByUser(Long id, User user);

}
