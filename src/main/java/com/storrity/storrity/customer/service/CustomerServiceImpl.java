/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.customer.service;

import com.storrity.storrity.customer.dto.CustomerCreationDto;
import com.storrity.storrity.customer.dto.CustomerUpdateDto;
import com.storrity.storrity.customer.entity.Customer;
import com.storrity.storrity.customer.entity.CustomerQueryParams;
import com.storrity.storrity.customer.repository.CustomerRepository;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class CustomerServiceImpl implements CustomerService{
    
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Transactional
    @Override
    public Customer create(CustomerCreationDto dto) {
        Customer customer = Customer.builder()
                .city(dto.getCity())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .state(dto.getState())
                .street(dto.getStreet())
                .build();
        
//        @Todo customer cash accounts here

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public Customer fetch(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Customer not found with id: " + id)); 
    }

    @Override
    public List<Customer> list(CustomerQueryParams params) {
        return customerRepository.list(params);
    }

    @Override
    public CountDto count(CustomerQueryParams params) {
        return CountDto
                .builder()
                .count(customerRepository.countRecords(params))
                .build();
    }

    @Transactional
    @Override
    public Customer update(UUID id, CustomerUpdateDto dto) {
        Customer c = customerRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Customer not found with id: " + id));
        if(dto.getFullName() != null){
            c.setFullName(dto.getFullName());
        }
        
        if(dto.getPhone()!= null){
            c.setPhone(dto.getPhone());
        }
        
        if(dto.getEmail()!= null){
            c.setEmail(dto.getEmail());
        }
        
        if(dto.getFullName() != null){
            c.setFullName(dto.getFullName());
        }
        
        if(dto.getState()!= null){
            c.setState(dto.getState());
        }
        
        if(dto.getCity()!= null){
            c.setCity(dto.getCity());
        }
        
        if(dto.getStreet()!= null){
            c.setStreet(dto.getStreet());
        }
        
        Customer savedCustomer = customerRepository.save(c);
        return savedCustomer;
    }

    @Transactional
    @Override
    public Customer delete(UUID id) {
        Customer c = customerRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Customer not found with id: " + id));
        customerRepository.delete(c);
//        eventPublisher.publishEvent(new ProductDeletedEvent(p));
        return c;
    }
    
}
