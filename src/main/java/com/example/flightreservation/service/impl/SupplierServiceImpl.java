package com.example.flightreservation.service.impl;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.model.dto.SupplierDto;
import com.example.flightreservation.model.entity.Supplier;
import com.example.flightreservation.model.mapper.SupplierMapper;
import com.example.flightreservation.model.request.CreateSupplierRequest;
import com.example.flightreservation.repository.SupplierRepository;
import com.example.flightreservation.service.SupplierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierServiceImpl implements SupplierService {

    SupplierRepository supplierRepository;
    SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierDto create(CreateSupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .supplierName(request.getSupplierName())
                .build();
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findEntityByName(String name) {
        return supplierRepository.findSupplierBySupplierName(name)
                .orElseThrow(() -> new EntityNotFoundException("Supplier " + name + " not found"));
    }
}
