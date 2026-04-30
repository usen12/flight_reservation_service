package com.example.flightreservation.service.impl;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.model.dto.CityDto;
import com.example.flightreservation.model.entity.City;
import com.example.flightreservation.model.mapper.CityMapper;
import com.example.flightreservation.model.request.CreateCityRequest;
import com.example.flightreservation.repository.CityRepository;
import com.example.flightreservation.service.CityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityServiceImpl implements CityService {

    CityRepository cityRepository;
    CityMapper cityMapper;

    @Override
    @Transactional
    public CityDto create(CreateCityRequest request) {
        City city = City.builder()
                .cityName(request.getCityName())
                .lat(request.getLat())
                .lon(request.getLon())
                .state(request.getState())
                .build();
        return cityMapper.toDto(cityRepository.save(city));
    }

    @Override
    @Transactional(readOnly = true)
    public City findEntityByStateAndName(String state, String cityName) {
        return cityRepository.findCityByStateAndCityName(state, cityName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "City with name=" + cityName + " and state=" + state + " not found"));
    }
}
