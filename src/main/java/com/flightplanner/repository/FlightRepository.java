package com.flightplanner.repository;

import com.flightplanner.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query; 

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    List<Flight> findByDestination(String destination);
    
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Flight> findByPriceLessThanEqual(double maxPrice);

    @Query("SELECT DISTINCT f.destination FROM Flight f WHERE LOWER(f.destination) LIKE LOWER(CONCAT(?1, '%')) ORDER BY f.destination ASC")
    List<String> findDestinationSuggestions(String prefix);
}