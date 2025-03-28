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
import java.util.logging.Logger;

@Service
public class FlightService {
    private static final Logger logger = Logger.getLogger(FlightService.class.getName());
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
        logger.info("Searching flights with max price: " + maxPrice);
        List<Flight> flights = flightRepository.findByPriceLessThanEqual(maxPrice);
        logger.info("Found " + flights.size() + " flights");
        return flights;
    }

    public List<Flight> getFlightsByDepartureDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return flightRepository.findByDepartureTimeBetween(startOfDay, endOfDay);
    }

    public List<Flight> searchFlights(String destination, LocalDate departureDate, Double maxPrice) {
        try {
            logger.info("Search parameters - destination: " + destination + 
                      ", departureDate: " + departureDate + 
                      ", maxPrice: " + maxPrice);
                      
            // Kõige lihtsam lähenemine - hakka algusest peale
            List<Flight> results = getAllFlights();
            logger.info("Total flights in database: " + results.size());
            
            // Filtreeri sihtkoha järgi kui vaja
            if (destination != null && !destination.isEmpty()) {
                results = results.stream()
                         .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                         .toList();
                logger.info("After destination filter: " + results.size() + " matches");
            }
            
            // Filtreeri kuupäeva järgi kui vaja
            if (departureDate != null) {
                results = results.stream()
                         .filter(flight -> {
                             LocalDate flightDate = flight.getDepartureTime().toLocalDate();
                             return flightDate.equals(departureDate);
                         })
                         .toList();
                logger.info("After date filter: " + results.size() + " matches");
            }
            
            // Filtreeri hinna järgi kui vaja
            if (maxPrice != null) {
                results = results.stream()
                         .filter(flight -> flight.getPrice() <= maxPrice)
                         .toList();
                logger.info("After price filter: " + results.size() + " matches");
            }
            
            return results;
        } catch (Exception e) {
            logger.severe("Error searching flights: " + e.getMessage());
            e.printStackTrace();
            // Tagasta tühi nimekiri vea korral, et kasutajale näidata tühja tulemust
            return List.of();
        }
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}