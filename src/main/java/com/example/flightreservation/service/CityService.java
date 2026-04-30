package com.example.flightreservation.service;

import com.example.flightreservation.model.dto.CityDto;
import com.example.flightreservation.model.entity.City;
import com.example.flightreservation.model.request.CreateCityRequest;

public interface CityService {
    CityDto create(CreateCityRequest request);
    City findEntityByStateAndName(String state, String cityName);
}
