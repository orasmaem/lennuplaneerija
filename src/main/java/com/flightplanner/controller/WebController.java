package com.flightplanner.controller;

import com.flightplanner.model.Flight;
import com.flightplanner.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class WebController {

    private final FlightService flightService;
    
    @Autowired
    public WebController(FlightService flightService) {
        this.flightService = flightService;
    }
    
    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) Double maxPrice,
            Model model) {
        
        List<Flight> flights;
        
        if (destination != null || departureDate != null || maxPrice != null) {
            flights = flightService.searchFlights(destination, departureDate, maxPrice);
        } else {
            flights = flightService.getAllFlights();
        }
        
        model.addAttribute("flights", flights);
        return "index";
    }
}