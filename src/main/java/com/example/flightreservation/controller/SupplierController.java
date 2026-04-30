package com.example.flightreservation.controller;

import com.example.flightreservation.model.request.CreateSupplierRequest;
import com.example.flightreservation.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Suppliers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplier")
public class SupplierController {

    @NonNull SupplierService supplierService;

    @Operation(summary = "Create a new supplier")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateSupplierRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(supplierService.create(request));
    }
}
