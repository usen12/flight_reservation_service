package com.example.flightreservation.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_flights")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  Flight extends BaseEntity {

    @Column(name = "flight_date", nullable = false)
    LocalDate flightDate;

    @Column(name = "departure_time", nullable = false)
    LocalTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    LocalTime arrivalTime;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "city_from_id", referencedColumnName = "id")
    City from;

    @ManyToOne
    @JoinColumn(name = "city_to_id", referencedColumnName = "id")
    City to;

    @ManyToOne
    @JoinColumn(name = "plane_id", referencedColumnName = "id")
    Plane plane;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_booking",
            joinColumns = @JoinColumn(name = "flight_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"))
    List<Customer> passengers;
}
