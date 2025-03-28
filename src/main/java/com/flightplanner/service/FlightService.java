package com.flightplanner.service;

import com.flightplanner.model.Flight;
import com.flightplanner.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public List<Flight> getFlightsByDestination(String destination) {
        return flightRepository.findByDestination(destination);
    }

    public List<Flight> getFlightsByPriceRange(double maxPrice) {
        return flightRepository.findByPriceLessThanEqual(maxPrice);
    }

    public List<Flight> getFlightsByDepartureDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return flightRepository.findByDepartureTimeBetween(startOfDay, endOfDay);
    }

    public List<Flight> searchFlights(String destination, LocalDate departureDate, Double maxPrice) {
        LocalDateTime departureDateTime = departureDate != null ? departureDate.atStartOfDay() : null;
        return flightRepository.findFlightsByFilters(destination, departureDateTime, maxPrice);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}