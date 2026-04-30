package com.example.flightreservation.model.mapper;


import java.util.List;

public interface BaseMapper<ENTITY, DTO> {
    ENTITY toEntity(DTO dto);
    DTO toDto(ENTITY entity);
    List<DTO> toDtoList(List<ENTITY> entities);
    List<ENTITY> toEntityList(List<DTO> dtos);
}
