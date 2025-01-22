// package com.example.travelagency.Service.Impl;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.travelagency.Entity.HotelBooking;
// import com.example.travelagency.Entity.Hotels;
// import com.example.travelagency.Entity.Passenger;
// import com.example.travelagency.Entity.User;
// import com.example.travelagency.Repository.HotelBookingRepository;
// import com.example.travelagency.Repository.HotelRepository;
// import com.example.travelagency.Service.HotelBookingService;

// @Service
// public class HotelBookingServiceImpl implements HotelBookingService {

//     @Autowired
//     private HotelRepository hotelRepository;

//     @Autowired
//     private HotelBookingRepository hotelBookingRepository;

//     @Override
//     public void saveBooking(String hotelname, List<Passenger> passengers, String cityname, User user) {
//         Hotels hotel = hotelRepository.findByHotelname(hotelname);
//         if (hotel == null) {
//             throw new IllegalArgumentException("Hotel not found.");
//         }

//         int requiredRooms = (int) Math.ceil(passengers.size() / 2.0);
//         // Check if there are enough rooms available
//         if (hotel.getAvailableRooms() < requiredRooms) {
//             throw new IllegalArgumentException("Not enough rooms available.");
//         }

//         // Update available rooms in the hotel
//         hotel.setAvailableRooms(hotel.getAvailableRooms() - requiredRooms);
//         hotelRepository.save(hotel);

//         // Save the booking
//         for (Passenger passenger : passengers) {
//             HotelBooking booking = new HotelBooking();
//             booking.setCityname(cityname);
//             booking.setHotelname(hotelname);
//             booking.setName(passenger.getName());
//             booking.setPhone(passenger.getPhone());
//             booking.setNoofrooms(requiredRooms); // Set the number of rooms booked
//             booking.setUser(user); // Set the user for the booking
//             hotelBookingRepository.save(booking);
//         }
//     }

//     @Override
//     public List<HotelBooking> findBookingsByUser(User user) {
//         return hotelBookingRepository.findByUser(user);
//     }

//     @Override
//     public void deleteBooking(Long id, User user) {
//         HotelBooking booking = hotelBookingRepository.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

//         if (!booking.getUser().equals(user)) {
//             throw new IllegalArgumentException("User not authorized to delete this booking");
//         }

//         Hotels hotel = hotelRepository.findByHotelname(booking.getHotelname());
//         if (hotel != null) {
//             // Increase the available rooms
//             hotel.setAvailableRooms(hotel.getAvailableRooms() + booking.getNoofrooms());
//             hotelRepository.save(hotel); // Save the updated hotel entity
//         }

//         hotelBookingRepository.delete(booking);
//     }
// }
package com.example.travelagency.Service.Impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.travelagency.Entity.HotelBooking;
import com.example.travelagency.Entity.Hotels;
import com.example.travelagency.Entity.Passenger;
import com.example.travelagency.Entity.User;
import com.example.travelagency.Repository.HotelBookingRepository;
import com.example.travelagency.Repository.HotelRepository;
import com.example.travelagency.Service.HotelBookingService;

@Service
public class HotelBookingServiceImpl implements HotelBookingService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    @Override
    public void saveBooking(String hotelname, List<Passenger> passengers, String cityname, LocalDate checkIn, LocalDate checkOut, User user) {
        Hotels hotel = hotelRepository.findByHotelname(hotelname);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel not found.");
        }

        int requiredRooms = (int) Math.ceil(passengers.size() / 2.0);
        // Check if there are enough rooms available
        if (hotel.getAvailableRooms() < requiredRooms) {
            throw new IllegalArgumentException("Not enough rooms available.");
        }

        // Update available rooms in the hotel
        hotel.setAvailableRooms(hotel.getAvailableRooms() - requiredRooms);
        hotelRepository.save(hotel);

        // Save the booking
        for (Passenger passenger : passengers) {
            HotelBooking booking = new HotelBooking();
            booking.setCityname(cityname);
            booking.setHotelname(hotelname);
            booking.setCheckIn(checkIn); // Set the check-in date
            booking.setCheckOut(checkOut); // Set the check-out date
            booking.setName(passenger.getName());
            booking.setPhone(passenger.getPhone());
            booking.setNoofrooms(requiredRooms); // Set the number of rooms booked
            booking.setUser(user); // Set the user for the booking
            hotelBookingRepository.save(booking);
        }
    }

    @Override
    public List<HotelBooking> findBookingsByUser(User user) {
        return hotelBookingRepository.findByUser(user);
    }
     @Override
    public List<HotelBooking> findAllBookings() {
        return hotelBookingRepository.findAll();
    }
    @Override
public void deleteHotelBooking(Long id, User user) {
    System.out.println("Attempting to find booking with ID: " + id);
    HotelBooking booking = hotelBookingRepository.findById(id).orElse(null);
    if (booking != null) {
        System.out.println("Booking found: " + booking);
        User bookingUser = booking.getUser();
        if ((bookingUser != null && bookingUser.equals(user)) || "am9982061@gmail.com".equals(user.getUsername())) {
            // Handle the booking deletion
            hotelBookingRepository.delete(booking);
            System.out.println("Booking deleted successfully.");
        } else {
            throw new IllegalArgumentException("Booking not found or user not authorized.");
        }
    } else {
        throw new IllegalArgumentException("Booking not found.");
    }
}



    @Override
    public boolean isBookingOwnedByUser(Long id, User user) {
        return hotelBookingRepository.findByIdAndUser(id, user).isPresent();
    }
}
