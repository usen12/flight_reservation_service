package com.example.flightreservation.service;

import com.example.flightreservation.model.dto.PlaneDto;
import com.example.flightreservation.model.entity.Plane;
import com.example.flightreservation.model.request.CreatePlaneRequest;

public interface PlaneService {
    PlaneDto create(CreatePlaneRequest request);
    Plane findEntityById(Long id);
}
