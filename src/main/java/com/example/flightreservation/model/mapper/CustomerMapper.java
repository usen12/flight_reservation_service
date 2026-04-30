package com.example.flightreservation.model.mapper;

import com.example.flightreservation.model.dto.CustomerDto;
import com.example.flightreservation.model.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends BaseMapper<Customer, CustomerDto> {
}
