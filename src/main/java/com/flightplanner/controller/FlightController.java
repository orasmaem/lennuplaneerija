package com.flightplanner.controller;

import com.flightplanner.model.Flight;
import com.flightplanner.model.dto.FlightDTO;
import com.flightplanner.model.dto.SearchCriteriaDTO;
import com.flightplanner.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        List<FlightDTO> flightDTOs = flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) Double maxPrice) {
        
        List<Flight> flights = flightService.searchFlights(destination, departureDate, maxPrice);
        List<FlightDTO> flightDTOs = flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightDTOs);
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlightsPost(@RequestBody SearchCriteriaDTO criteria) {
        List<Flight> flights = flightService.searchFlights(
                criteria.getDestination(),
                criteria.getDepartureDate(),
                criteria.getMaxPrice());
        
        List<FlightDTO> flightDTOs = flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightDTOs);
    }

    private FlightDTO convertToDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setAirline(flight.getAirline());
        dto.setOrigin(flight.getOrigin());
        dto.setDestination(flight.getDestination());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setPrice(flight.getPrice());
        return dto;
    }
}