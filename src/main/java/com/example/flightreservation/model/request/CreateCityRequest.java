package com.example.flightreservation.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCityRequest {

    @NotBlank
    String cityName;

    @NotBlank
    String state;

    @NotNull
    @DecimalMin("-90.000000")
    @DecimalMax("90.000000")
    BigDecimal lat;

    @NotNull
    @DecimalMin("-180.000000")
    @DecimalMax("180.000000")
    BigDecimal lon;
}
