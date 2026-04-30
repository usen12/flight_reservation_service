package com.example.flightreservation.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFlightRequest {

    @NotNull
    LocalDate flightDate;

    @NotNull
    LocalTime departureTime;

    @NotNull
    LocalTime arrivalTime;

    @NotNull
    @Positive
    BigDecimal price;

    @NotBlank
    String stateFrom;

    @NotBlank
    String cityFrom;

    @NotBlank
    String stateTo;

    @NotBlank
    String cityTo;

    @NotNull
    @Positive
    Long planeId;
}
