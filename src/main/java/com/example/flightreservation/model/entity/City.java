package com.example.flightreservation.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class City extends BaseEntity {

    @Column(name = "city_name", nullable = false)
    String cityName;

    @Column(name = "state", nullable = false)
    String state;

    @Column(name = "lat", nullable = false)
    BigDecimal lat;

    @Column(name = "lon", nullable = false)
    BigDecimal lon;

}
