package com.example.flightreservation.service.impl;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.model.dto.CustomerDto;
import com.example.flightreservation.model.entity.Customer;
import com.example.flightreservation.model.entity.Wallet;
import com.example.flightreservation.model.mapper.CustomerMapper;
import com.example.flightreservation.model.request.CreateCustomerRequest;
import com.example.flightreservation.repository.CustomerRepository;
import com.example.flightreservation.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerDto create(CreateCustomerRequest request) {
        String fullName = request.getFirstName() + " "
                + (request.getPatronymic() != null ? request.getPatronymic() + " " : "")
                + request.getLastName();

        Customer customer = Customer.builder()
                .wallet(Wallet.builder().funds(request.getFunds()).build())
                .phoneNumber(request.getPhoneNumber())
                .patronymic(request.getPatronymic())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .fullName(fullName)
                .build();

        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        return customerMapper.toDto(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id=" + id + " not found"));
    }
}
