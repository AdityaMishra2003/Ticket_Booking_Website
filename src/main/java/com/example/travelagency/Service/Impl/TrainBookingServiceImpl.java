package com.example.travelagency.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;


import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.Train;
import com.example.travelagency.Entity.TrainBooking;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Repository.TrainBookingRepository;
import com.example.travelagency.Repository.TrainRepository;
import com.example.travelagency.Service.TrainBookingService;

@Service
public class TrainBookingServiceImpl implements TrainBookingService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TrainBookingRepository trainBookingRepository;

    @Override
public void saveBooking(String trainnumber, String trainname, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,List<Passenger> passengers, User user) {
    Train train = trainRepository.findByTrainnumber(trainnumber);
    if (train == null) {
        throw new IllegalArgumentException("Train not found.");
    }

    // Check if there are enough seats available
    if (train.getAvailableSeats() < passengers.size()) {
        throw new IllegalArgumentException("Not enough seats available.");
    }

    for (Passenger passenger : passengers) {
        TrainBooking booking = new TrainBooking();
        booking.setTrainnumber(trainnumber);
        booking.setTrainname(train.getTrainname());
        booking.setDepartureDate(departureDate);
        booking.setName(passenger.getName());
        booking.setPhone(passenger.getPhone());
        booking.setNumberOfPersons(passengers.size());
        booking.setUser(user);
        trainBookingRepository.save(booking);
    }

    train.reduceSeats(passengers.size());
    trainRepository.save(train);
}


    @Override
    public List<TrainBooking> findBookingsByUser(User user) {
        return trainBookingRepository.findByUser(user);
    }
     @Override
    public List<TrainBooking> findAllBookings() {
        return trainBookingRepository.findAll();
    }
    @Override
    public void deleteTrainBooking(Long id, User user) {
        System.out.println("Attempting to find train booking with ID: " + id);
        TrainBooking booking = trainBookingRepository.findById(id).orElse(null);
        if (booking != null) {
            System.out.println("Train booking found: " + booking);
            User bookingUser = booking.getUser();
            if ((bookingUser != null && bookingUser.equals(user)) || "am9982061@gmail.com".equals(user.getUsername())) {
                Train train = trainRepository.findByTrainnumber(booking.getTrainnumber());
                if (train != null) {
                    train.increaseSeats(1);
                    trainRepository.save(train);
                    System.out.println("Updated train seats and saved successfully.");
                }
                trainBookingRepository.delete(booking);
                System.out.println("Train booking deleted successfully.");
            } else {
                throw new IllegalArgumentException("Booking not found or user not authorized.");
            }
        } else {
            throw new IllegalArgumentException("Train booking not found.");
        }
    }
    

    @Override
    public boolean isBookingOwnedByUser(Long id, User user) {
        return trainBookingRepository.findByIdAndUser(id, user).isPresent();
    }
}
