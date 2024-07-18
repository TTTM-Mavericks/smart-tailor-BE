package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.PaymentAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(PaymentAPI.PAYMENT)
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    @GetMapping(PaymentAPI.PAYMENT_INFO + "/{paymentID}")
    ResponseEntity<ObjectNode> getPaymentInfo(@PathVariable("paymentID") UUID paymentID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var data = paymentService.getPaymentByID(paymentID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(data));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN PAYMENT CONTROLLER: {}", ex.getMessage());
            throw ex;
        }
    }
}
