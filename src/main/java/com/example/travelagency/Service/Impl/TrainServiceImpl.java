package com.example.travelagency.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.travelagency.Entity.Train;
import com.example.travelagency.Repository.TrainRepository;
import com.example.travelagency.Service.TrainService;

@Service
public class TrainServiceImpl implements TrainService {
    @Autowired
    private TrainRepository trainRepository;

    @Override
    public List<Train> findTrains(String source, String destination, String departureDay) {
        return trainRepository.findBySourceAndDestinationAndDepartureDay(source, destination, departureDay);
    }

    @Override
    public void updateAvailableSeats(String trainnumber, int availableSeats) {
        Train train = trainRepository.findByTrainnumber(trainnumber);
        if (train != null) {
            train.setAvailableSeats(availableSeats); // Assuming setAvailableSeats is the correct setter method
            trainRepository.save(train);
        } else {
            throw new RuntimeException("Train not found with number: " + trainnumber);
        }
    }
    @Override
    public int getAvailableSeats(String trainnumber) {
        Train train = trainRepository.findByTrainnumber(trainnumber);
        if (train != null) {
            return train.getAvailableSeats();
        } else {
            throw new RuntimeException("Train not found with number: " + trainnumber);
        }
    }
}
