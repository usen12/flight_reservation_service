package com.example.flightreservation.service;

import com.example.flightreservation.model.dto.FlightDto;
import com.example.flightreservation.model.request.CreateBookingRequest;
import com.example.flightreservation.model.request.CreateFlightRequest;
import com.example.flightreservation.model.response.CustomerFlightResponse;
import com.example.flightreservation.model.response.MessageResponse;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    FlightDto create(CreateFlightRequest request);
    List<CustomerFlightResponse> findAll(LocalDate currentDate, String cityFrom, String cityTo);
    MessageResponse book(CreateBookingRequest request);
    List<CustomerFlightResponse> getFlightsByCustomers(Long customerId);
    MessageResponse cancelBooking(CreateBookingRequest request);
}
