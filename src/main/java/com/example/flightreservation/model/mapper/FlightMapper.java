package com.example.flightreservation.model.mapper;

import com.example.flightreservation.model.dto.FlightDto;
import com.example.flightreservation.model.entity.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper extends BaseMapper<Flight, FlightDto> {
}
