package com.example.flightreservation.repository;

import com.example.flightreservation.model.entity.Customer;
import com.example.flightreservation.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByFlightDateAndFromCityNameAndToCityName(LocalDate flightDate, String cityFrom, String cityTo);

    @Query(value =
            "SELECT * FROM tb_flights f " +
            "JOIN tb_booking b ON f.id = b.flight_id " +
            "JOIN tb_customer c ON c.id = b.customer_id " +
            "WHERE c.id = ?1",
            nativeQuery = true)
    List<Flight> findAllByPassenger(Customer customer);

    @Modifying
    @Query(value = "DELETE FROM tb_booking WHERE customer_id = ?1 AND flight_id = ?2", nativeQuery = true)
    void deleteFromBooking(Long customerId, Long flightId);
}
