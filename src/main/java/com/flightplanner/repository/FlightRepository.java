package com.flightplanner.repository;

import com.flightplanner.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    List<Flight> findByDestination(String destination);
    
    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Flight> findByPriceLessThanEqual(double maxPrice);
    
    @Query("SELECT f FROM Flight f WHERE " +
           "(:destination IS NULL OR f.destination = :destination) AND " +
           "(:departureDate IS NULL OR CAST(f.departureTime AS date) = :departureDate) AND " +
           "(:maxPrice IS NULL OR f.price <= :maxPrice)")
    List<Flight> findFlightsByFilters(
            @Param("destination") String destination,
            @Param("departureDate") LocalDateTime departureDate,
            @Param("maxPrice") Double maxPrice);
}