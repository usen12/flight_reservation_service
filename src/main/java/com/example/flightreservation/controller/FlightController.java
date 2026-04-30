package com.example.flightreservation.controller;

import com.example.flightreservation.model.request.CreateBookingRequest;
import com.example.flightreservation.model.request.CreateFlightRequest;
import com.example.flightreservation.model.response.CustomerFlightResponse;
import com.example.flightreservation.model.response.MessageResponse;
import com.example.flightreservation.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Flights")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flight")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightController {

    @NonNull FlightService flightService;

    @Operation(summary = "Create a new flight")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateFlightRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(flightService.create(request));
    }

    @Operation(summary = "Search available flights by date and city")
    @GetMapping("/find-all")
    public ResponseEntity<?> findAll(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate currentDate,
                                     @RequestParam("from") String cityFrom,
                                     @RequestParam("to") String cityTo) {
        List<CustomerFlightResponse> result = flightService.findAll(currentDate, cityFrom, cityTo);
        if (result.isEmpty()) {
            return ResponseEntity.ok(MessageResponse.sendMessage("No available flights found"));
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Book a flight for a customer")
    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(flightService.book(request));
    }

    @Operation(summary = "Cancel a flight booking")
    @DeleteMapping("/delete")
    public ResponseEntity<?> cancelBooking(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(flightService.cancelBooking(request));
    }
}
