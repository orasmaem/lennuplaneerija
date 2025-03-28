package com.flightplanner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaDTO {
    private String destination;
    private LocalDate departureDate;
    private Double maxPrice;
}