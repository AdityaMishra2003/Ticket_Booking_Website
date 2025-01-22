package com.example.travelagency.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.example.travelagency.Entity.Flight;
import com.example.travelagency.Entity.FlightBooking;
import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Repository.FlightBookingRepository;
import com.example.travelagency.Repository.FlightRepository;
import com.example.travelagency.Service.FlightBookingService;

@Service
public class FlightBookingServiceImpl implements FlightBookingService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightBookingRepository flightBookingRepository;

    @Override
    public void saveBooking(String flightnumber, String flightname, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, List<Passenger> passengers, User user) {
        Flight flight = flightRepository.findByFlightnumber(flightnumber);
        
        // Log to check if flight is found
        System.out.println("Flight found: " + (flight != null ? flight.toString() : "null"));

        if (flight == null) {
            throw new IllegalArgumentException("Flight not found.");
        }

        if (flight.getAvailableSeats() < passengers.size()) {
            throw new IllegalArgumentException("Not enough seats available.");
        }

        for (Passenger passenger : passengers) {
            FlightBooking booking = new FlightBooking();
            booking.setFlightnumber(flightnumber);
            booking.setFlightname(flight.getFlightname());
            booking.setDepartureDate(departureDate);
            booking.setName(passenger.getName());
            booking.setPhone(passenger.getPhone());
            booking.setNumberOfPersons(passengers.size());
            booking.setUser(user);
            flightBookingRepository.save(booking);
        }

        flight.reduceSeats(passengers.size());
        flightRepository.save(flight);
    }

    @Override
    public List<FlightBooking> findBookingsByUser(User user) {
        return flightBookingRepository.findByUser(user);
    }
     @Override
    public List<FlightBooking> findAllBookings() {
        return flightBookingRepository.findAll();
    }
    @Override
public void deleteFlightBooking(Long id, User user) {
    System.out.println("Attempting to find flight booking with ID: " + id);
    FlightBooking booking = flightBookingRepository.findById(id).orElse(null);
    if (booking != null) {
        System.out.println("Flight booking found: " + booking);
        User bookingUser = booking.getUser();
        if ((bookingUser != null && bookingUser.equals(user)) || "am9982061@gmail.com".equals(user.getUsername())) {
            Flight flight = flightRepository.findByFlightnumber(booking.getFlightnumber());
            if (flight != null) {
                flight.increaseSeats(1);
                flightRepository.save(flight);
                System.out.println("Updated flight seats and saved successfully.");
            }
            flightBookingRepository.delete(booking);
            System.out.println("Flight booking deleted successfully.");
        } else {
            throw new IllegalArgumentException("Booking not found or user not authorized.");
        }
    } else {
        throw new IllegalArgumentException("Flight booking not found.");
    }
}

    
    
    @Override
    public boolean isBookingOwnedByUser(Long id, User user) {
        return flightBookingRepository.findByIdAndUser(id, user).isPresent();
    }
}
