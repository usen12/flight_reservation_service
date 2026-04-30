package com.example.flightreservation.model.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCustomerRequest {

    @Email
    String email;

    @Length(min = 2)
    String firstName;

    @Length(min = 2)
    String lastName;

    String patronymic;

    @Length(min = 10, max = 14)
    String phoneNumber;

    @PositiveOrZero
    BigDecimal funds;
}