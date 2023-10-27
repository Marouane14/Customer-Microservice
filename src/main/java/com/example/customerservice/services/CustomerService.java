package com.example.customerservice.services;

import com.example.customerservice.dto.CustomerDTO;
import com.example.customerservice.dto.CustomerRequestDTO;
import com.example.customerservice.dto.CustomerResponseDTO;
import com.example.customerservice.exceptions.CustomerNotFoundException;
import com.example.customerservice.exceptions.EmailAlreadyUsedException;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO save(CustomerRequestDTO request) throws EmailAlreadyUsedException;
    List<CustomerResponseDTO> listCustomers();
    CustomerResponseDTO getCustomerById(Long id) throws CustomerNotFoundException;
    CustomerResponseDTO update(CustomerRequestDTO requestDTO) throws CustomerNotFoundException, EmailAlreadyUsedException;
    void deleteCustomer(Long id) throws CustomerNotFoundException;
    List<CustomerResponseDTO> findCustomers(String keyWord);
}
