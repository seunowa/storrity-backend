/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.customer.service;

import com.storrity.storrity.customer.dto.CustomerCreationDto;
import com.storrity.storrity.customer.dto.CustomerUpdateDto;
import com.storrity.storrity.customer.entity.Customer;
import com.storrity.storrity.customer.entity.CustomerQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface CustomerService {
    public Customer create(CustomerCreationDto dto);
    public Customer fetch(UUID id);
    public List<Customer> list(CustomerQueryParams params);
    public CountDto count(CustomerQueryParams params);
    public Customer update(UUID id, CustomerUpdateDto dto);
    public Customer delete(UUID id);
}
