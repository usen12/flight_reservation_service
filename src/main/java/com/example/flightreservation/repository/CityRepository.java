package com.example.flightreservation.repository;


import com.example.flightreservation.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findCityByStateAndCityName(String state, String cityName);

}