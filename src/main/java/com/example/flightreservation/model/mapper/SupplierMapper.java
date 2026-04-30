package com.example.flightreservation.model.mapper;

import com.example.flightreservation.model.dto.SupplierDto;
import com.example.flightreservation.model.entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends BaseMapper<Supplier, SupplierDto> {
}
