package com.example.travelagency.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelagency.Entity.Hotels;

public interface HotelRepository extends JpaRepository<Hotels, Long> {
    Hotels findByHotelname(String hotelname);
    List<Hotels> findByCityname(String cityname);  // This should match the entity field
}

