package com.example.flightreservation.controller;

import com.example.flightreservation.model.dto.CustomerDto;
import com.example.flightreservation.model.request.CreateCustomerRequest;
import com.example.flightreservation.model.response.CustomerResponse;
import com.example.flightreservation.service.CustomerService;
import com.example.flightreservation.service.FlightService;
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

@Tag(name = "Customers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerController {

    @NonNull CustomerService customerService;
    @NonNull FlightService flightService;

    @Operation(summary = "Register a new customer")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.create(request));
    }

    @Operation(summary = "Get customer details with flight history")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        CustomerDto customer = customerService.getById(id);
        CustomerResponse response = CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .patronymic(customer.getPatronymic())
                .fullName(customer.getFullName())
                .phoneNumber(customer.getPhoneNumber())
                .funds(customer.getWallet() != null ? customer.getWallet().getFunds() : null)
                .flightResponseList(flightService.getFlightsByCustomers(id))
                .build();
        return ResponseEntity.ok(response);
    }
}
