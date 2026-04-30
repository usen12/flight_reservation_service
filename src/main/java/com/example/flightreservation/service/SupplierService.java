package com.example.flightreservation.service;

import com.example.flightreservation.model.dto.SupplierDto;
import com.example.flightreservation.model.entity.Supplier;
import com.example.flightreservation.model.request.CreateSupplierRequest;

public interface SupplierService {
    SupplierDto create(CreateSupplierRequest request);
    Supplier findEntityByName(String name);
}
