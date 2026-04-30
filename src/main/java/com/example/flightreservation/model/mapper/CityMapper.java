package com.example.flightreservation.model.mapper;

import com.example.flightreservation.model.dto.CityDto;
import com.example.flightreservation.model.entity.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper extends BaseMapper<City, CityDto> {
}
