package com.flightplanner.config;

import com.flightplanner.model.Flight;
import com.flightplanner.service.FlightService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader {

    private final FlightService flightService;
    private final Random random = new Random();

    @Autowired
    public DataLoader(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostConstruct
    public void loadData() {
        List<Flight> flights = generateSampleFlights();
        for (Flight flight : flights) {
            flightService.saveFlight(flight);
        }
    }

    private List<Flight> generateSampleFlights() {
        List<Flight> flights = new ArrayList<>();
        
        String[] airlines = {"Estonian Air", "Finnair", "Lufthansa", "British Airways", "Air France", "KLM"};
        String[] origins = {"Tallinn", "Helsinki", "Frankfurt", "London", "Paris", "Amsterdam"};
        String[] destinations = {"New York", "Tokyo", "Sydney", "Rome", "Barcelona", "Dubai", "Singapore", "Cape Town"};
        
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < 20; i++) {
            Flight flight = new Flight();
            
            int airlineIndex = random.nextInt(airlines.length);
            flight.setAirline(airlines[airlineIndex]);
            flight.setOrigin(origins[random.nextInt(origins.length)]);
            flight.setDestination(destinations[random.nextInt(destinations.length)]);
            
            // Generate random flight number
            flight.setFlightNumber(airlines[airlineIndex].substring(0, 2).toUpperCase() + (100 + random.nextInt(900)));
            
            // Generate random departure time within the next 30 days
            int daysOffset = random.nextInt(30);
            int hoursOffset = random.nextInt(24);
            int minutesOffset = random.nextInt(4) * 15; // 0, 15, 30, or 45 minutes
            
            LocalDateTime departureTime = now.plusDays(daysOffset).plusHours(hoursOffset).withMinute(minutesOffset);
            flight.setDepartureTime(departureTime);
            
            // Generate random flight duration between 1 and 15 hours
            int flightDurationHours = 1 + random.nextInt(14);
            int flightDurationMinutes = random.nextInt(4) * 15; // 0, 15, 30, or 45 minutes
            
            LocalDateTime arrivalTime = departureTime.plusHours(flightDurationHours).plusMinutes(flightDurationMinutes);
            flight.setArrivalTime(arrivalTime);
            
            // Generate random price between 100 and 1500 EUR
            double price = 100.0 + random.nextInt(1400);
            flight.setPrice(price);
            
            flights.add(flight);
        }
        
        return flights;
    }
}