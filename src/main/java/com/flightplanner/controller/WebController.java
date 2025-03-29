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
import java.util.logging.Logger;

@Controller
public class WebController {
    private static final Logger logger = Logger.getLogger(WebController.class.getName());
    private final FlightService flightService;
    
    @Autowired
    public WebController(FlightService flightService) {
        this.flightService = flightService;
    }
    
    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            Model model) {
        
        logger.info("Received request params - destination: " + destination + 
                    ", departureDate: " + departureDate + 
                    ", minPrice: " + minPrice +
                    ", maxPrice: " + maxPrice);
        
        // Parse minPrice
        Double minPriceValue = null;
        if (minPrice != null && !minPrice.isEmpty()) {
            try {
                //Proovime parsida Double väärtuseks, asendades koma punktiga
                minPriceValue = Double.parseDouble(minPrice.replace(",", "."));
                logger.info("Parsed minPrice to: " + minPriceValue);
            } catch (NumberFormatException e) {
                logger.warning("Could not parse minPrice: " + minPrice + " - " + e.getMessage());
                //Ei tee midagi, minPriceValue jääb null'iks
            }
        }
        
        // Parse maxPrice
        Double maxPriceValue = null;
        if (maxPrice != null && !maxPrice.isEmpty()) {
            try {
                //Proovime parsida Double väärtuseks, asendades koma punktiga
                maxPriceValue = Double.parseDouble(maxPrice.replace(",", "."));
                logger.info("Parsed maxPrice to: " + maxPriceValue);
            } catch (NumberFormatException e) {
                logger.warning("Could not parse maxPrice: " + maxPrice + " - " + e.getMessage());
                //Ei tee midagi, maxPriceValue jääb null'iks
            }
        }
        
        List<Flight> flights;
        
        try {
            //Kui mingi filter on määratud, siis otsib, muidu tagastab kõik lennud
            if (destination != null || departureDate != null || minPriceValue != null || maxPriceValue != null) {
                flights = flightService.searchFlights(destination, departureDate, minPriceValue, maxPriceValue);
            } else {
                flights = flightService.getAllFlights();
            }
            
            logger.info("Found " + flights.size() + " flights");
        } catch (Exception e) {
            logger.severe("Error processing request: " + e.getMessage());
            e.printStackTrace();
            flights = flightService.getAllFlights(); // Default to all flights on error
        }
        //Paneme tulemused mudelisse ja kuvame index.html lehel
        model.addAttribute("flights", flights);
        return "index";
    }
}