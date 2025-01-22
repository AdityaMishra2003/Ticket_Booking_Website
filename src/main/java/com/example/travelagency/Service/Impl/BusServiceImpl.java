package com.example.travelagency.Service.Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.travelagency.Entity.Bus;
import com.example.travelagency.Repository.BusRepository;
import com.example.travelagency.Service.BusService;

import java.util.List;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public List<Bus> findBuses(String source, String destination, String departureDay) {
        return busRepository.findBySourceAndDestinationAndDepartureDay(source, destination, departureDay);
    }
    @Override
    public void updateAvailableSeats(String busNumber, int availableSeats) {
        Bus bus = busRepository.findByBusnumber(busNumber); 
        if (bus != null) {
            bus.setAvailableSeats(availableSeats);
            busRepository.save(bus);
        } else {
            throw new RuntimeException("Bus not found with number: " + busNumber);
        }
    }
    @Override
    public int getAvailableSeats(String busNumber) {
        Bus bus = busRepository.findByBusnumber(busNumber); 
        if (bus != null) {
            return bus.getAvailableSeats();
        } else {
            throw new RuntimeException("Bus not found with number: " + busNumber);
        }
    }
    
}
