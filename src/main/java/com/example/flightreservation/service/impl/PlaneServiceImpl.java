package com.example.flightreservation.service.impl;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.model.dto.PlaneDto;
import com.example.flightreservation.model.entity.Plane;
import com.example.flightreservation.model.mapper.PlaneMapper;
import com.example.flightreservation.model.request.CreatePlaneRequest;
import com.example.flightreservation.repository.PlaneRepository;
import com.example.flightreservation.service.PlaneService;
import com.example.flightreservation.service.SupplierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaneServiceImpl implements PlaneService {

    PlaneRepository planeRepository;
    SupplierService supplierService;
    PlaneMapper planeMapper;

    @Override
    @Transactional
    public PlaneDto create(CreatePlaneRequest request) {
        Plane plane = Plane.builder()
                .supplier(supplierService.findEntityByName(request.getSupplier()))
                .capacity(request.getCapacity())
                .model(request.getModel())
                .build();
        return planeMapper.toDto(planeRepository.save(plane));
    }

    @Override
    @Transactional(readOnly = true)
    public Plane findEntityById(Long id) {
        return planeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plane with id=" + id + " not found"));
    }
}
