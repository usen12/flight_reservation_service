package com.example.flightreservation.model.mapper;

import com.example.flightreservation.model.dto.PlaneDto;
import com.example.flightreservation.model.entity.Plane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaneMapper extends BaseMapper<Plane, PlaneDto> {
}
