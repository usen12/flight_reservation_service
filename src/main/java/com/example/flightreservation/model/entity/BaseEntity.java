package com.example.flightreservation.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    Long id;

    @Column(name = "date_created", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    LocalDateTime dateCreated;

    @Column(name = "date_update", columnDefinition = "TIMESTAMP")
    LocalDateTime dateUpdated;

    @PrePersist
    private void onCreate() {
        dateCreated = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }
}