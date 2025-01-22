package com.example.travelagency.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {

    @Autowired
    private BusBookingService bookingService;
    @Autowired
    private HotelBookingService hotelBookingService;
    @Autowired
    private TrainBookingService trainBookingService;
    @Autowired
    private FlightBookingService flightBookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    private User getUserFromAuthentication(Authentication authentication) {
        String username = null;
        User user = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            username = (String) attributes.get("email");
            if (username == null) {
                username = (String) attributes.get("login");
            }
            System.out.println("User logged in via OAuth2: " + attributes);
            user = userService.findByUsername(username);
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            System.out.println("User logged in via traditional login: " + username);
            user = userService.findByUsername(username);
        }

        return user;
    }

    @PostMapping("/bookBus")
    public String showPassengerDetailsForm(@RequestParam String busnumber, @RequestParam int numberOfPersons, Model model) {
        model.addAttribute("busNumber", busnumber);
        model.addAttribute("numberOfPersons", numberOfPersons);
        return "passengerDetails";
    }

    @PostMapping("/processBooking")
    public String processBooking(@RequestParam String busNumber, @RequestParam int numberOfPersons,
                                 @RequestParam List<String> name, @RequestParam List<String> phone,
                                 Authentication authentication, Model model) {
        User user = getUserFromAuthentication(authentication);

        if (user == null) {
            System.out.println("User not found in the database");
            return "errorPage";
        }

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            Passenger passenger = new Passenger();
            passenger.setName(name.get(i));
            passenger.setPhone(phone.get(i));
            passengers.add(passenger);
        }

        bookingService.saveBooking(busNumber, passengers, user);

        String subject = "Bus Booking Confirmation";
        String body = String.format("Dear %s,\n\nYour booking for bus number %s has been confirmed.\n\nPassengers:\n%s",
                user.getUsername(), busNumber, passengers.stream()
                        .map(p -> p.getName() + " - " + p.getPhone())
                        .reduce((p1, p2) -> p1 + "\n" + p2)
                        .orElse("No passengers"));

        emailService.sendBookingConfirmation(user.getUsername(), subject, body);

        return "bookingConfirmation";
    }

    @PostMapping("/bookHotels")
    public String showHotelPassengerDetailsForm(@RequestParam String hotelname, @RequestParam String cityname,
                                                @RequestParam String checkIn, @RequestParam String checkOut,
                                                @RequestParam int numberOfPersons, Model model) {
        model.addAttribute("hotelname", hotelname);
        model.addAttribute("cityname", cityname);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("numberOfPersons", numberOfPersons);
        return "hotelPassengerDetails";
    }

    @PostMapping("/processHotelBooking")
    public String processHotelBooking(@RequestParam String hotelname, @RequestParam String cityname,
                                      @RequestParam String checkIn, @RequestParam String checkOut,
                                      @RequestParam int numberOfPersons, @RequestParam List<String> name,
                                      @RequestParam List<String> phone,
                                      Authentication authentication, Model model) {
        User user = getUserFromAuthentication(authentication);

        if (user == null) {
            System.out.println("User not found in the database");
            return "errorPage";
        }

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            Passenger passenger = new Passenger();
            passenger.setName(name.get(i));
            passenger.setPhone(phone.get(i));
            passengers.add(passenger);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);

        hotelBookingService.saveBooking(hotelname, passengers, cityname, checkInDate, checkOutDate, user);

        String subject = "Hotel Booking Confirmation";
        String body = String.format("Dear %s,\n\nYour booking at %s in %s has been confirmed.\nCheck-in: %s\nCheck-out: %s\n\nPassengers:\n%s",
                user.getUsername(), hotelname, cityname, checkIn, checkOut,
                passengers.stream()
                        .map(p -> p.getName() + " - " + p.getPhone())
                        .reduce((p1, p2) -> p1 + "\n" + p2)
                        .orElse("No passengers"));

        emailService.sendBookingConfirmation(user.getUsername(), subject, body);

        return "bookingConfirmation";
    }

    @PostMapping("/bookTrains")
    public String showTrainPassengerDetailsForm(@RequestParam String trainname, @RequestParam String trainnumber,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, 
                                                @RequestParam int numberOfPersons, Model model) {
        model.addAttribute("trainname", trainname);
        model.addAttribute("trainnumber", trainnumber);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("numberOfPersons", numberOfPersons);
        return "TrainPassengerDetails";
    }

    @PostMapping("/processTrainBooking")
    public String processTrainBooking(@RequestParam String trainname, @RequestParam String trainnumber,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                      @RequestParam int numberOfPersons, @RequestParam List<String> name,
                                      @RequestParam List<String> phone,
                                      Authentication authentication, Model model) {
        User user = getUserFromAuthentication(authentication);

        if (user == null) {
            System.out.println("User not found in the database");
            return "errorPage";
        }

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            Passenger passenger = new Passenger();
            passenger.setName(name.get(i));
            passenger.setPhone(phone.get(i));
            passengers.add(passenger);
        }

        trainBookingService.saveBooking(trainnumber, trainname, departureDate, passengers, user);

        String subject = "Train Booking Confirmation";
        String body = String.format("Dear %s,\n\nYour booking for train number %s in %s has been confirmed.\nJourney Date: %s\n\nPassengers:\n%s",
                user.getUsername(), trainnumber, trainname, departureDate,
                passengers.stream()
                        .map(p -> p.getName() + " - " + p.getPhone())
                        .reduce((p1, p2) -> p1 + "\n" + p2)
                        .orElse("No passengers"));

        emailService.sendBookingConfirmation(user.getUsername(), subject, body);

        return "bookingConfirmation";
    }

    @PostMapping("/bookFlights")
    public String showFlightPassengerDetailsForm(@RequestParam String flightnumber, @RequestParam String flightname,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, 
                                                 @RequestParam int numberOfPersons, Model model) {
        model.addAttribute("flightname", flightname);
        model.addAttribute("flightnumber", flightnumber);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("numberOfPersons", numberOfPersons);
        return "FlightPassengerDetails";
    }

    @PostMapping("/processFlightBooking")
    public String processFlightBooking(@RequestParam String flightname, @RequestParam String flightnumber,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                       @RequestParam int numberOfPersons, @RequestParam List<String> name,
                                       @RequestParam List<String> phone,
                                       Authentication authentication, Model model) {
        User user = getUserFromAuthentication(authentication);

        if (user == null) {
            System.out.println("User not found in the database");
            return "errorPage";
        }

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            Passenger passenger = new Passenger();
            passenger.setName(name.get(i));
            passenger.setPhone(phone.get(i));
            passengers.add(passenger);
        }

        flightBookingService.saveBooking(flightnumber, flightname, departureDate, passengers, user);

        String subject = "Flight Booking Confirmation";
        String body = String.format("Dear %s,\n\nYour booking for flight number %s in %s has been confirmed.\nJourney Date: %s\n\nPassengers:\n%s",
                user.getUsername(), flightnumber, flightname, departureDate,
                passengers.stream()
                        .map(p -> p.getName() + " - " + p.getPhone())
                        .reduce((p1, p2) -> p1 + "\n" + p2)
                        .orElse("No passengers"));

        emailService.sendBookingConfirmation(user.getUsername(), subject, body);

        return "bookingConfirmation";
    }
}
