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
        
        String[] airlines = {"Estonian Air", "Finnair", "Lufthansa", "British Airways", "Air France"};
        String[] origins = {"Tallinn"};
        String[] destinations = {"Riia", "Helsinki", "Frankfurt", "London", "Pariis", "Amsterdam", "Stockholm", "Oslo", "Kopenhaagen", "Berliin", "Brüssel", "Milano", "Barcelona", "Madrid", "Rooma"};
        
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < 40; i++) {
            Flight flight = new Flight();
            
            int airlineIndex = random.nextInt(airlines.length);
            flight.setAirline(airlines[airlineIndex]);
            flight.setOrigin(origins[random.nextInt(origins.length)]);
            flight.setDestination(destinations[random.nextInt(destinations.length)]);
            
            // Lennu number
            flight.setFlightNumber(airlines[airlineIndex].substring(0, 2).toUpperCase() + (100 + random.nextInt(900)));
            
            // Väljumis aeg
            int daysOffset = random.nextInt(30);
            int hoursOffset = random.nextInt(24);
            int minutesOffset = random.nextInt(4) * 15; 
            
            LocalDateTime departureTime = now.plusDays(daysOffset).plusHours(hoursOffset).withMinute(minutesOffset);
            flight.setDepartureTime(departureTime);
            
            // Lennu pikkus
            int flightDurationHours = 1 + random.nextInt(14);
            int flightDurationMinutes = random.nextInt(4) * 15; 
            
            LocalDateTime arrivalTime = departureTime.plusHours(flightDurationHours).plusMinutes(flightDurationMinutes);
            flight.setArrivalTime(arrivalTime);
            
            // Lennu hind
            double price = 100.0 + random.nextInt(1400);
            flight.setPrice(price);
            
            flights.add(flight);
        }
        
        return flights;
    }
}