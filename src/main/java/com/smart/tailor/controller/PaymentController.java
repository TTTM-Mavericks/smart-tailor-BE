package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.PaymentAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.service.PaymentService;
import com.smart.tailor.utils.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(PaymentAPI.PAYMENT)
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;
    private final PayOSService payOSService;

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

    @GetMapping(PaymentAPI.CONFIRM_PAYMENT + "/{paymentID}")
    ResponseEntity<ObjectNode> confirmPayment(@PathVariable("paymentID") Integer paymentID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            payOSService.confirmPayment(paymentID);
            response.put("status", 200);
            response.put("message", MessageConstant.CONFIRM_PAYMENT_SUCCESSFULLY);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN PAYMENT CONTROLLER: {}", ex.getMessage());
            throw ex;
        }
    }

    @PostMapping(PaymentAPI.CREATE_PAYMENT)
    ResponseEntity<ObjectNode> createPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.createManualPayment(paymentRequest);
            response.put("status", 200);
            response.put("message", MessageConstant.CREATE_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR WHEN CREATE MANUAL PAYMENT!", ex.getMessage());
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.MANUAL_PAYMENT_INFO + "/{paymentID}")
    ResponseEntity<ObjectNode> getManualPaymentByID(@PathVariable("paymentID") UUID paymentID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.getManualPaymentByID(paymentID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
