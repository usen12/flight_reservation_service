package com.example.flightreservation.controller;

import com.example.flightreservation.model.request.CreateCityRequest;
import com.example.flightreservation.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cities")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/city")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityController {

    @NonNull CityService cityService;

    @Operation(summary = "Create a new city")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateCityRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cityService.create(request));
    }
}
