package com.example.flightreservation.service;

import com.example.flightreservation.model.dto.CustomerDto;
import com.example.flightreservation.model.entity.Customer;
import com.example.flightreservation.model.request.CreateCustomerRequest;

public interface CustomerService {
    CustomerDto create(CreateCustomerRequest request);
    CustomerDto getById(Long id);
    Customer findEntityById(Long id);
}
