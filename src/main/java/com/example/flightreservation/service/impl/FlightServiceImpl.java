package com.example.flightreservation.service.impl;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.exceptions.FlightBookingException;
import com.example.flightreservation.exceptions.FundsException;
import com.example.flightreservation.model.dto.FlightDto;
import com.example.flightreservation.model.entity.City;
import com.example.flightreservation.model.entity.Customer;
import com.example.flightreservation.model.entity.Flight;
import com.example.flightreservation.model.entity.Plane;
import com.example.flightreservation.model.mapper.FlightMapper;
import com.example.flightreservation.model.request.CreateBookingRequest;
import com.example.flightreservation.model.request.CreateFlightRequest;
import com.example.flightreservation.model.response.CityResponse;
import com.example.flightreservation.model.response.CustomerFlightResponse;
import com.example.flightreservation.model.response.MessageResponse;
import com.example.flightreservation.model.response.PlaneResponse;
import com.example.flightreservation.repository.FlightRepository;
import com.example.flightreservation.service.CityService;
import com.example.flightreservation.service.CustomerService;
import com.example.flightreservation.service.FlightService;
import com.example.flightreservation.service.PlaneService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;
    PlaneService planeService;
    CityService cityService;
    CustomerService customerService;
    FlightMapper flightMapper;

    @Override
    @Transactional
    public FlightDto create(CreateFlightRequest request) {
        Plane plane = planeService.findEntityById(request.getPlaneId());
        City to = cityService.findEntityByStateAndName(request.getStateTo(), request.getCityTo());
        City from = cityService.findEntityByStateAndName(request.getStateFrom(), request.getCityFrom());

        Flight flight = Flight.builder()
                .to(to)
                .from(from)
                .flightDate(request.getFlightDate())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .plane(plane)
                .price(request.getPrice())
                .build();

        return flightMapper.toDto(flightRepository.save(flight));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerFlightResponse> findAll(LocalDate currentDate, String cityFrom, String cityTo) {
        return flightRepository
                .findAllByFlightDateAndFromCityNameAndToCityName(currentDate, cityFrom, cityTo)
                .stream()
                .map(this::mapFlightToResponse)
                .toList();
    }

    @Override
    @Transactional
    public MessageResponse book(CreateBookingRequest request) {
        Flight flight = flightRepository
                .findById(request.getFlightId())
                .orElseThrow(() -> new EntityNotFoundException("Flight with id=" + request.getFlightId() + " not found"));

        Customer customer = customerService.findEntityById(request.getCustomerId());

        int capacity = flight.getPlane().getCapacity();
        int booked = flight.getPassengers().size();
        if (booked >= capacity) {
            throw new FlightBookingException("No available seats: capacity=" + capacity + " filled=" + booked);
        }

        if (customer.getWallet().getFunds().compareTo(flight.getPrice()) < 0) {
            throw new FundsException("Available funds=" + customer.getWallet().getFunds() + " ticket price=" + flight.getPrice());
        }

        flight.getPassengers().add(customer);
        customer.getWallet().setFunds(customer.getWallet().getFunds().subtract(flight.getPrice()));
        flightRepository.save(flight);

        return MessageResponse.sendMessage("Booking successful");
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerFlightResponse> getFlightsByCustomers(Long customerId) {
        Customer customer = customerService.findEntityById(customerId);
        return flightRepository
                .findAllByPassenger(customer)
                .stream()
                .map(this::mapFlightToResponse)
                .toList();
    }

    @Override
    @Transactional
    public MessageResponse cancelBooking(CreateBookingRequest request) {
        flightRepository.deleteFromBooking(request.getCustomerId(), request.getFlightId());
        return MessageResponse.sendMessage("Flight canceled");
    }

    private CustomerFlightResponse mapFlightToResponse(Flight flight) {
        return CustomerFlightResponse.builder()
                .id(flight.getId())
                .flightDate(flight.getFlightDate())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .price(flight.getPrice())
                .from(CityResponse.builder()
                        .state(flight.getFrom().getState())
                        .cityName(flight.getFrom().getCityName())
                        .build())
                .to(CityResponse.builder()
                        .state(flight.getTo().getState())
                        .cityName(flight.getTo().getCityName())
                        .build())
                .plane(PlaneResponse.builder()
                        .supplierName(flight.getPlane().getSupplier().getSupplierName())
                        .model(flight.getPlane().getModel())
                        .build())
                .build();
    }
}
