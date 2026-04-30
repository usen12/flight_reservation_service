package com.example.flightreservation.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePlaneRequest {

    @NotBlank
    String model;

    @NotNull
    @Positive
    Integer capacity;

    @NotBlank
    String supplier;
}
