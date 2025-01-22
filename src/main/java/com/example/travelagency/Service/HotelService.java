package com.example.travelagency.Service;


import java.util.List;


import com.example.travelagency.Entity.Hotels;

public interface HotelService {
     List<Hotels> findHotels(String cityname);
     void updateAvailableRooms(Long id,String hotelname,int availableRooms);
     int getAvailableRooms(Long id,String hotelname);
}
