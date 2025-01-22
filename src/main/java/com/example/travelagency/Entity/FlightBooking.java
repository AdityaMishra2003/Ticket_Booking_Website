package com.example.travelagency.Entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class FlightBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightname;
    private String flightnumber;
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate;
    private String departureDay;
    private String name;
    private String phone;
    private int numberOfPersons;
    @ManyToOne
    private User user;
}

