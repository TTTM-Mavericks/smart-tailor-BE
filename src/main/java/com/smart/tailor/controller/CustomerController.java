package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.CustomerService;
import com.smart.tailor.utils.request.CustomerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.CustomerAPI.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final ObjectMapper objectMapper;

    @PutMapping(APIConstant.CustomerAPI.UPDATE_CUSTOMER_PROFILE)
    public ResponseEntity<ObjectNode> updateCustomerProfile(@RequestBody CustomerRequest customerRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var message = customerService.updateCustomerProfile(customerRequest);
            if (message.equals(MessageConstant.UPDATE_PROFILE_CUSTOMER_SUCCESSFULLY)) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.UPDATE_PROFILE_CUSTOMER_SUCCESSFULLY);
            } else {
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", message);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN CREATE MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
