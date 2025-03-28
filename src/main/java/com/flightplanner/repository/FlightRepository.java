package com.flightplanner.repository;

import com.flightplanner.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    List<Flight> findByDestination(String destination);
    
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Flight> findByPriceLessThanEqual(double maxPrice);
}