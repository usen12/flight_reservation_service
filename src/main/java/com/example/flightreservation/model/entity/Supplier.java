package com.example.flightreservation.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier extends BaseEntity {

    @Column(name = "supplier_name", unique = true, nullable = false)
    String supplierName;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    List<Plane> planeList = new java.util.ArrayList<>();
}
