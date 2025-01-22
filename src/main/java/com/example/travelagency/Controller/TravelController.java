package com.example.travelagency.Controller;

import com.example.travelagency.Entity.Bus;
import com.example.travelagency.Entity.Flight;
import com.example.travelagency.Entity.Hotels;
import com.example.travelagency.Entity.Train;
import com.example.travelagency.Service.BusService;
import com.example.travelagency.Service.FlightService;
import com.example.travelagency.Service.HotelService;
import com.example.travelagency.Service.TrainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class TravelController {

    @Autowired
    private BusService busService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private HotelService hotelService;
    @PostMapping("/searchBus")
    public String searchBus(@RequestParam("busSrc") String busSrc,
                            @RequestParam("busDest") String busDest,
                            @RequestParam("departureDate") String departureDate,
                            Model model) {
   
    // Convert the departureDate String to LocalDate
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse(departureDate, formatter);

    // Get the day of the week from the LocalDate
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    String departureDay = dayOfWeek.name(); // Convert to String

        List<Bus> buses = busService.findBuses(busSrc, busDest, departureDay);
        model.addAttribute("buses", buses);
        model.addAttribute("departureDate", departureDate);
        return "searchBus"; // Thymeleaf template name to display results
    }
    @PostMapping("/searchHotels")
    public String searchHotels(@RequestParam("cityname") String cityname,
                           @RequestParam("checkIn") String checkIn,
                           @RequestParam("checkOut") String checkOut,
                           Model model) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate checkin = LocalDate.parse(checkIn, formatter);
    LocalDate checkout = LocalDate.parse(checkOut, formatter);

    System.out.println("Received cityname: " + cityname);
    List<Hotels> hotels = hotelService.findHotels(cityname);
    model.addAttribute("Hotels", hotels);
    model.addAttribute("checkIn", checkin);
    model.addAttribute("checkOut", checkout);
    return "searchHotels";
    }
    @PostMapping("/searchTrains")
    public String searchTrains(@RequestParam("source") String source,
                           @RequestParam("destination") String destination,
                           @RequestParam("departureDate") String departureDate,
                           Model model) {

    // Convert the departureDate String to LocalDate
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse(departureDate, formatter);

    // Get the day of the week from the LocalDate
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    String departureDay = dayOfWeek.name(); // Convert to String

    List<Train> trains = trainService.findTrains(source, destination, departureDay);

    // Add departureDate to the model
    model.addAttribute("trains", trains);
    model.addAttribute("departureDate", departureDate);
    return "searchTrains";
    }
    @PostMapping("/searchFlights")
public String searchFlights(@RequestParam("source") String source,
                            @RequestParam("destination") String destination,
                            @RequestParam("departureDate") String departureDate,
                            Model model) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse(departureDate, formatter);

    DayOfWeek dayOfWeek = date.getDayOfWeek();
    String departureDay = dayOfWeek.name(); // Convert to String

    List<Flight> flights = flightService.findFlights(source, destination, departureDay);

    // Add departureDate and flights to the model
    model.addAttribute("flights", flights);
    model.addAttribute("departureDate", departureDate);
    return "searchFlights";
}

    
    
    
    // Similarly, add methods for searchHotel, searchTrain, searchFlight
}
