package com.example.travelagency.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.travelagency.Entity.FlightBooking;
import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;

public interface FlightBookingService {
     void saveBooking(String flightnumber,String flightname,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, List<Passenger> passengers, User user);
    List<FlightBooking> findBookingsByUser(User user);
     List<FlightBooking> findAllBookings();
    void deleteFlightBooking(Long id, User user);
    boolean isBookingOwnedByUser(Long id, User user);

}
