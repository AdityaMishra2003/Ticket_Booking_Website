package com.example.travelagency.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.travelagency.Entity.User;
import com.example.travelagency.Service.BusBookingService;
import com.example.travelagency.Service.FlightBookingService;
import com.example.travelagency.Service.HotelBookingService;
import com.example.travelagency.Service.TrainBookingService;
import com.example.travelagency.Service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusBookingService busBookingService;
    
    @Autowired
    private HotelBookingService hotelBookingService;
    
    @Autowired
    private TrainBookingService trainBookingService;

    @Autowired
    private FlightBookingService flightBookingService;

    @GetMapping
    public String getAccountPage(Authentication authentication, Model model) {
        User user = getAuthenticatedUser(authentication);
        if (user != null) {
            model.addAttribute("user", user);
            if ("am9982061@gmail.com".equals(user.getUsername())) {
                // Fetch all bookings if the user is am9982061@gmail.com
                model.addAttribute("bookings", busBookingService.findAllBookings());
                model.addAttribute("hotelBookings", hotelBookingService.findAllBookings());
                model.addAttribute("trainBookings", trainBookingService.findAllBookings());
                model.addAttribute("flightBookings", flightBookingService.findAllBookings());
            } else {
                // Fetch only user's bookings if the user is not am9982061@gmail.com
                model.addAttribute("bookings", busBookingService.findBookingsByUser(user));
                model.addAttribute("hotelBookings", hotelBookingService.findBookingsByUser(user));
                model.addAttribute("trainBookings", trainBookingService.findBookingsByUser(user));
                model.addAttribute("flightBookings", flightBookingService.findBookingsByUser(user));
            }
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "error";
        }
        
        return "account";
    }
    
    @PostMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user != null && isUserAuthorizedToDeleteBooking(id, user, busBookingService)) {
            busBookingService.deleteBooking(id, user);
        }
        return "redirect:/account";
    }
    
    @PostMapping("/deleteHotelBooking/{id}")
    public String deleteHotelBooking(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user != null && isUserAuthorizedToDeleteBooking(id, user, hotelBookingService)) {
            hotelBookingService.deleteHotelBooking(id, user);
        }
        return "redirect:/account";
    }

    @PostMapping("/deleteTrainBooking/{id}")
    public String deleteTrainBooking(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user != null && isUserAuthorizedToDeleteBooking(id, user, trainBookingService)) {
            trainBookingService.deleteTrainBooking(id, user);
        }
        return "redirect:/account";
    }
    
    @PostMapping("/deleteFlightBooking/{id}")
    public String deleteFlightBooking(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        if (user != null && isUserAuthorizedToDeleteBooking(id, user, flightBookingService)) {
            flightBookingService.deleteFlightBooking(id, user);
        }
        return "redirect:/account";
    }
    
    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oAuth2Token.getPrincipal();
            String email = oAuth2User.getAttribute("email"); // Adjust based on OAuth2 provider
            return userService.findByUsername(email);
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.findByUsername(userDetails.getUsername());
        }
        return null;
    }
    
    private boolean isUserAuthorizedToDeleteBooking(Long bookingId, User user, BusBookingService bookingService) {
        // Allow deletion if the user is "am9982061@gmail.com" or if the booking belongs to the user
        return "am9982061@gmail.com".equals(user.getUsername()) || bookingService.isBookingOwnedByUser(bookingId, user);
    }
    private boolean isUserAuthorizedToDeleteBooking(Long bookingId, User user, FlightBookingService bookingService) {
        // Allow deletion if the user is "am9982061@gmail.com" or if the booking belongs to the user
        return "am9982061@gmail.com".equals(user.getUsername()) || bookingService.isBookingOwnedByUser(bookingId, user);
    }
    private boolean isUserAuthorizedToDeleteBooking(Long bookingId, User user, HotelBookingService bookingService) {
        // Allow deletion if the user is "am9982061@gmail.com" or if the booking belongs to the user
        return "am9982061@gmail.com".equals(user.getUsername()) || bookingService.isBookingOwnedByUser(bookingId, user);
    }
    private boolean isUserAuthorizedToDeleteBooking(Long bookingId, User user, TrainBookingService bookingService) {
        // Allow deletion if the user is "am9982061@gmail.com" or if the booking belongs to the user
        return "am9982061@gmail.com".equals(user.getUsername()) || bookingService.isBookingOwnedByUser(bookingId, user);
    }
}
