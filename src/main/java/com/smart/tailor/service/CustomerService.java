package com.smart.tailor.service;

import com.smart.tailor.entities.Customer;
import com.smart.tailor.entities.User;
import com.smart.tailor.utils.request.CustomerRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CustomerResponse;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    void createCustomer(User user, Boolean gender);

    Optional<Customer> findById(UUID customerID);

    APIResponse updateCustomerProfile(CustomerRequest customerRequest);

    CustomerResponse getCustomerByUserID(UUID userID);

    CustomerResponse mapperToCustomerResponse(Customer customer);
}
