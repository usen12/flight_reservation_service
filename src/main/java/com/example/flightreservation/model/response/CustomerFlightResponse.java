package com.example.flightreservation.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerFlightResponse {
    Long id;
    LocalDate flightDate;
    LocalTime departureTime;
    LocalTime arrivalTime;
    BigDecimal price;
    CityResponse from;
    CityResponse to;
    PlaneResponse plane;
}
