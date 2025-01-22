package com.example.travelagency.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.travelagency.Entity.Hotels;
import com.example.travelagency.Repository.HotelRepository;
import com.example.travelagency.Service.HotelService;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<Hotels> findHotels(String cityname) {
        return hotelRepository.findByCityname(cityname);

    }

    @Override
    public void updateAvailableRooms(Long id,String hotelname, int availableRooms) {
       Hotels hotels = hotelRepository.findByHotelname(hotelname);
        if (hotels != null) {
            hotels.setAvailableRooms(availableRooms);
            hotelRepository.save(hotels);
        } else {
            throw new RuntimeException("Hotel Not found With name: " + hotelname);
        }
    }

    @Override
    public int getAvailableRooms(Long id,String hotelname) {
        Hotels hotels = hotelRepository.findByHotelname(hotelname);
        if (hotels != null) {
            return hotels.getAvailableRooms();
        } else {
            throw new RuntimeException("Bus not found with number: " + hotelname);
        }
    }
    
}
