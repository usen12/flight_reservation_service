package com.example.flightreservation.service;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.exceptions.FlightBookingException;
import com.example.flightreservation.exceptions.FundsException;
import com.example.flightreservation.model.entity.*;
import com.example.flightreservation.model.mapper.FlightMapper;
import com.example.flightreservation.model.request.CreateBookingRequest;
import com.example.flightreservation.model.response.MessageResponse;
import com.example.flightreservation.repository.FlightRepository;
import com.example.flightreservation.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock FlightRepository flightRepository;
    @Mock PlaneService planeService;
    @Mock CityService cityService;
    @Mock CustomerService customerService;
    @Mock FlightMapper flightMapper;

    @InjectMocks FlightServiceImpl flightService;

    private Flight flight;
    private Customer customer;
    private Wallet wallet;
    private CreateBookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        Supplier supplier = Supplier.builder().supplierName("AirTest").build();
        Plane plane = Plane.builder().model("Boeing 737").capacity(2).supplier(supplier).build();

        City cityFrom = City.builder().cityName("New York").state("NY").lat(BigDecimal.ZERO).lon(BigDecimal.ZERO).build();
        City cityTo = City.builder().cityName("Los Angeles").state("CA").lat(BigDecimal.ZERO).lon(BigDecimal.ZERO).build();

        wallet = Wallet.builder().funds(new BigDecimal("500.00")).build();
        customer = Customer.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .wallet(wallet)
                .build();

        flight = Flight.builder()
                .flightDate(LocalDate.now().plusDays(1))
                .departureTime(LocalTime.of(10, 0))
                .arrivalTime(LocalTime.of(14, 0))
                .price(new BigDecimal("150.00"))
                .from(cityFrom)
                .to(cityTo)
                .plane(plane)
                .passengers(new ArrayList<>())
                .build();

        bookingRequest = new CreateBookingRequest();
        bookingRequest.setCustomerId(1L);
        bookingRequest.setFlightId(1L);
    }

    @Test
    void book_success() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(customerService.findEntityById(1L)).thenReturn(customer);
        when(flightRepository.save(any())).thenReturn(flight);

        MessageResponse response = flightService.book(bookingRequest);

        assertThat(response.getMessage()).isEqualTo("Booking successful");
        assertThat(flight.getPassengers()).contains(customer);
        assertThat(customer.getWallet().getFunds()).isEqualByComparingTo("350.00");
        verify(flightRepository).save(flight);
    }

    @Test
    void book_flightNotFound_throwsEntityNotFoundException() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.book(bookingRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Flight with id=1 not found");
    }

    @Test
    void book_flightFull_throwsFlightBookingException() {
        // Fill the plane (capacity = 2)
        Supplier supplier = Supplier.builder().supplierName("AirTest").build();
        Plane fullPlane = Plane.builder().model("Boeing 737").capacity(1).supplier(supplier).build();
        flight = Flight.builder()
                .flightDate(LocalDate.now().plusDays(1))
                .departureTime(LocalTime.of(10, 0))
                .arrivalTime(LocalTime.of(14, 0))
                .price(new BigDecimal("150.00"))
                .from(flight.getFrom())
                .to(flight.getTo())
                .plane(fullPlane)
                .passengers(new ArrayList<>(java.util.List.of(customer)))
                .build();

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(customerService.findEntityById(1L)).thenReturn(customer);

        assertThatThrownBy(() -> flightService.book(bookingRequest))
                .isInstanceOf(FlightBookingException.class)
                .hasMessageContaining("No available seats");
    }

    @Test
    void book_insufficientFunds_throwsFundsException() {
        customer.getWallet().setFunds(new BigDecimal("50.00")); // less than 150

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(customerService.findEntityById(1L)).thenReturn(customer);

        assertThatThrownBy(() -> flightService.book(bookingRequest))
                .isInstanceOf(FundsException.class)
                .hasMessageContaining("Available funds=50.00");
    }

    @Test
    void cancelBooking_callsDeleteFromBooking() {
        flightService.cancelBooking(bookingRequest);
        verify(flightRepository).deleteFromBooking(1L, 1L);
    }
}
