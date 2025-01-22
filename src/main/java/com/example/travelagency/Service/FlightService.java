package com.example.travelagency.Service;

import java.util.List;

import com.example.travelagency.Entity.Flight;

public interface FlightService {
    List<Flight> findFlights(String source, String destination, String departureDay);
    void updateAvailableSeats(String flightnumber, int availableSeats);
    int getAvailableSeats(String flightnumber);
}
