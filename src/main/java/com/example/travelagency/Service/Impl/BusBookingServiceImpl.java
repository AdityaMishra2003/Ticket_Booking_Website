package com.example.travelagency.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.travelagency.Entity.Bus;
import com.example.travelagency.Entity.BusBooking;
import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Repository.BusBookingRepository;
import com.example.travelagency.Repository.BusRepository;
import com.example.travelagency.Service.BusBookingService;

@Service
public class BusBookingServiceImpl implements BusBookingService {

    @Autowired
    private BusBookingRepository busBookingRepository;

    @Autowired
    private BusRepository busRepository;

    @Override
    public void saveBooking(String busNumber, List<Passenger> passengers, User user) {
        Bus bus = busRepository.findByBusnumber(busNumber); 
        if (bus == null) {
            throw new IllegalArgumentException("Bus not found.");
        }

        // Check if there are enough seats available
        if (bus.getAvailableSeats() < passengers.size()) {
            throw new IllegalArgumentException("Not enough seats available.");
        }

        for (Passenger passenger : passengers) {
            BusBooking booking = new BusBooking();
            booking.setBusnumber(busNumber);
            booking.setName(passenger.getName());
            booking.setPhone(passenger.getPhone());
            booking.setNumberOfPersons(passengers.size());
            booking.setUser(user); // Set the user for the booking
            busBookingRepository.save(booking);
        }

        // Reduce the seats after saving all bookings
        bus.reduceSeats(passengers.size());
        busRepository.save(bus); // Save the updated bus entity
    }

    @Override
    public List<BusBooking> findBookingsByUser(User user) {
        return busBookingRepository.findByUser(user);
    }
    @Override
    public List<BusBooking> findAllBookings() {
        return busBookingRepository.findAll();
    }

    @Override
    public void deleteBooking(Long id, User user) {
        BusBooking booking = busBookingRepository.findById(id).orElse(null);
        if (booking != null && (booking.getUser().equals(user) || "am9982061@gmail.com".equals(user.getUsername()))) {
            Bus bus = busRepository.findByBusnumber(booking.getBusnumber()); 
            if (bus != null) {
                bus.increaseSeats(booking.getNumberOfPersons());
                busRepository.save(bus); // Save the updated bus entity
            }
            busBookingRepository.delete(booking);
        } else {
            throw new IllegalArgumentException("Booking not found or user not authorized.");
        }
    }
    @Override
    public boolean isBookingOwnedByUser(Long id, User user) {
        return busBookingRepository.findByIdAndUser(id, user).isPresent();
    }
}
