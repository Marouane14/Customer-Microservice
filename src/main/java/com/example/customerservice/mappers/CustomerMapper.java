package com.example.customerservice.mappers;

import com.example.customerservice.dto.CustomerDTO;
import com.example.customerservice.dto.CustomerRequestDTO;
import com.example.customerservice.dto.CustomerResponseDTO;
import com.example.customerservice.entities.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    private ModelMapper modelMapper=new ModelMapper();
    //Mapper la classe Customer vers la classe CustomerDTO
    public CustomerResponseDTO from(Customer customer){
        return modelMapper.map(customer,CustomerResponseDTO.class);
    }
    public Customer from(CustomerRequestDTO customerRequestDTO){
        return modelMapper.map(customerRequestDTO,Customer.class);
    }
}
