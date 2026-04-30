package com.example.flightreservation.controller;

import com.example.flightreservation.model.request.CreatePlaneRequest;
import com.example.flightreservation.service.PlaneService;
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

@Tag(name = "Planes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plane")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneController {

    @NonNull PlaneService planeService;

    @Operation(summary = "Create a new plane")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreatePlaneRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(planeService.create(request));
    }
}
