package com.example.travelagency.Service;

import java.util.List;

import com.example.travelagency.Entity.Train;

public interface TrainService {
    List<Train> findTrains(String source, String destination, String departureDay);
    void updateAvailableSeats(String trainnumber, int availableSeats);
    int getAvailableSeats(String trainnumber);
}
