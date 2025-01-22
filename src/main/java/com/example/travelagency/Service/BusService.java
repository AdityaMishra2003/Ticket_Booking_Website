package com.example.travelagency.Service;


import java.util.List;

import com.example.travelagency.Entity.Bus;

public interface BusService {
    List<Bus> findBuses(String source, String destination, String departureDay);
    void updateAvailableSeats(String busNumber, int availableSeats);
    int getAvailableSeats(String busNumber);
}
