package com.example.customerservice.services;

import com.example.customerservice.dto.CustomerRequestDTO;
import com.example.customerservice.dto.CustomerResponseDTO;
import com.example.customerservice.entities.Customer;
import com.example.customerservice.exceptions.CustomerNotFoundException;
import com.example.customerservice.exceptions.EmailAlreadyUsedException;
import com.example.customerservice.mappers.CustomerMapper;
import com.example.customerservice.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    @Override
    public CustomerResponseDTO save(CustomerRequestDTO request) throws EmailAlreadyUsedException {
        if (customerRepository.checkIfEmailExists(request.getEmail()))
            throw new EmailAlreadyUsedException(String.format("This email %s is already used",request.getEmail()));
        Customer customer=customerMapper.from(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.from(savedCustomer);
    }

    @Override
    public List<CustomerResponseDTO> listCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::from).collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer==null) throw new CustomerNotFoundException(String.format("Customer Not Found :%s",id));
        return customerMapper.from(customer);
    }

    @Override
    public CustomerResponseDTO update(CustomerRequestDTO requestDTO) throws CustomerNotFoundException, EmailAlreadyUsedException {
        if (customerRepository.checkIfEmailExists(requestDTO.getEmail()))
            throw new EmailAlreadyUsedException(String.format("This email %s is already used",requestDTO.getEmail()));
        Customer customer = customerRepository.findById(requestDTO.getId()).orElse(null);
        if (customer==null) throw new CustomerNotFoundException(String.format("Customer Not Found :%s",requestDTO.getId()));
        if(requestDTO.getEmail()!=null) customer.setFirstName(requestDTO.getFirstName());
        if(requestDTO.getLastName()!=null) customer.setLastName(requestDTO.getLastName());
        if(requestDTO.getEmail()!=null) customer.setEmail(requestDTO.getEmail());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.from(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer==null) throw new CustomerNotFoundException(String.format("Customer Not Found :%s",id));
        customerRepository.deleteById(id);
    }
    @Override
    public List<CustomerResponseDTO> findCustomers(String keyWord) {
        List<Customer> customers = customerRepository.searchCustomers(keyWord);
        return customers.stream().map(customerMapper::from).collect(Collectors.toList());
    }

    //Cette méthode est utilisée pour génerer 5 Customers
    // après la construction et l'intialisation de notre application Spring Boot
    @PostConstruct
    public void populateData() throws EmailAlreadyUsedException {
        for (int i = 1; i <= 5; i++) {
            CustomerRequestDTO customerRequestDTO=new CustomerRequestDTO(null,"First Name "+i,"Last Name "+i,"email"+i+"@gmail.com");
            CustomerResponseDTO customerResponseDTO = save(customerRequestDTO);
        }
    }
}

