package com.smart.tailor.service;

import com.smart.tailor.entities.Customer;
import com.smart.tailor.utils.request.CustomerRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CustomerResponse;

import java.util.UUID;

public interface CustomerService {
    APIResponse updateCustomerProfile(CustomerRequest customerRequest);

    CustomerResponse getCustomerByUserID(UUID userID);

    CustomerResponse mapperToCustomerResponse(Customer customer);
}
