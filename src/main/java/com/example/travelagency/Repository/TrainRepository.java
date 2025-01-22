package com.example.travelagency.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelagency.Entity.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Train findByTrainnumber(String trainnumber);
    List<Train> findBySourceAndDestinationAndDepartureDay(String source, String destination, String departureDay);
}
