package com.example.travelagency.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.travelagency.Entity.Flight;
import com.example.travelagency.Repository.FlightRepository;
import com.example.travelagency.Service.FlightService;

@Service
public class FlightServiceImpl implements FlightService{

   @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<Flight> findFlights(String source, String destination, String departureDay) {
        return flightRepository.findBySourceAndDestinationAndDepartureDay(source, destination, departureDay);
    }

    @Override
    public void updateAvailableSeats(String flightnumber, int availableSeats) {
        Flight flight = flightRepository.findByFlightnumber(flightnumber);
        if (flight != null) {
            flight.setAvailableSeats(availableSeats); // Assuming setAvailableSeats is the correct setter method
            flightRepository.save(flight);
        } else {
            throw new RuntimeException("Flight not found with number: " + flightnumber);
        }
    }
    @Override
    public int getAvailableSeats(String flightnumber) {
        Flight flight = flightRepository.findByFlightnumber(flightnumber);
        if (flight != null) {
            return flight.getAvailableSeats();
        } else {
            throw new RuntimeException("Train not found with number: " + flightnumber);
        }
    }
    
}
