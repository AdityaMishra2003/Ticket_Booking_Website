package com.example.travelagency.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.travelagency.Entity.Bus;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Bus findByBusnumber(String busnumber);
    List<Bus> findBySourceAndDestinationAndDepartureDay(String source, String destination, String departureDay);
}
