/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.customer.repository;

import com.storrity.storrity.customer.entity.Customer;
import com.storrity.storrity.customer.entity.CustomerQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface CustomerRepositoryCustom {
    public List<Customer> list(CustomerQueryParams params);
    public Long countRecords (CustomerQueryParams params);
}
