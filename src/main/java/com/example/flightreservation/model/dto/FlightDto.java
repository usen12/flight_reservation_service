package com.example.flightreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlightDto implements Serializable {

    Long id;
    LocalDate flightDate;
    LocalTime departureTime;
    LocalTime arrivalTime;
    BigDecimal price;
    CityDto from;
    CityDto to;
    PlaneDto plane;
    List<CustomerDto> passengers;

    @JsonIgnore
    LocalDateTime dateCreated;

    @JsonIgnore
    LocalDateTime dateUpdated;
}
