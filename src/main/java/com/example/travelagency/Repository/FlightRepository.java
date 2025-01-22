package com.example.travelagency.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelagency.Entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Flight findByFlightnumber(String flightnumber);
    List<Flight> findBySourceAndDestinationAndDepartureDay(String source, String destination, String departureDay);
}
