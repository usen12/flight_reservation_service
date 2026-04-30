package com.example.flightreservation.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaneDto implements Serializable {

    Long id;
    String model;
    Integer capacity;
    SupplierDto supplier;

    @JsonIgnore
    LocalDateTime dateCreated;

    @JsonIgnore
    LocalDateTime dateUpdated;
}
