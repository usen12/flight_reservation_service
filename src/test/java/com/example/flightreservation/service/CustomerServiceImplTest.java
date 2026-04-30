package com.example.flightreservation.service;

import com.example.flightreservation.exceptions.EntityNotFoundException;
import com.example.flightreservation.model.dto.CustomerDto;
import com.example.flightreservation.model.dto.WalletDto;
import com.example.flightreservation.model.entity.Customer;
import com.example.flightreservation.model.entity.Wallet;
import com.example.flightreservation.model.mapper.CustomerMapper;
import com.example.flightreservation.model.request.CreateCustomerRequest;
import com.example.flightreservation.repository.CustomerRepository;
import com.example.flightreservation.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock CustomerRepository customerRepository;
    @Mock CustomerMapper customerMapper;

    @InjectMocks CustomerServiceImpl customerService;

    @Test
    void create_buildsFullNameAndSaves() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("jane@example.com");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setPatronymic("Ann");
        request.setPhoneNumber("9876543210");
        request.setFunds(new BigDecimal("100.00"));

        Customer saved = Customer.builder()
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .patronymic("Ann")
                .fullName("Jane Ann Doe")
                .phoneNumber("9876543210")
                .wallet(Wallet.builder().funds(new BigDecimal("100.00")).build())
                .build();

        CustomerDto expectedDto = CustomerDto.builder()
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .fullName("Jane Ann Doe")
                .wallet(WalletDto.builder().funds(new BigDecimal("100.00")).build())
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(saved);
        when(customerMapper.toDto(saved)).thenReturn(expectedDto);

        CustomerDto result = customerService.create(request);

        assertThat(result.getFullName()).isEqualTo("Jane Ann Doe");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void create_withoutPatronymic_buildsFullNameCorrectly() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("john@example.com");
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setPhoneNumber("1234567890");
        request.setFunds(BigDecimal.ZERO);

        Customer saved = Customer.builder()
                .email("john@example.com")
                .firstName("John")
                .lastName("Smith")
                .fullName("John Smith")
                .wallet(Wallet.builder().funds(BigDecimal.ZERO).build())
                .build();

        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            assertThat(c.getFullName()).isEqualTo("John Smith");
            return saved;
        });
        when(customerMapper.toDto(saved)).thenReturn(CustomerDto.builder().fullName("John Smith").build());

        CustomerDto result = customerService.create(request);
        assertThat(result.getFullName()).isEqualTo("John Smith");
    }

    @Test
    void findEntityById_notFound_throwsEntityNotFoundException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findEntityById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Customer with id=99 not found");
    }

    @Test
    void findEntityById_found_returnsCustomer() {
        Customer customer = Customer.builder().email("test@test.com").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findEntityById(1L);
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }
}
