package com.example.travelagency.Entity;


import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busnumber;
    private String busname;
    private String source;
    private String destination;
    private String departureDay; 
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int availableSeats;
    
    public void reduceSeats(int count) {
        this.availableSeats -= count;
    }

    public void increaseSeats(int count) {
        this.availableSeats += 1;
    }
}
