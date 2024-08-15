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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping(PaymentAPI.PAYMENT)
@Validated
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;
    private final PayOSService payOSService;

    @GetMapping(PaymentAPI.PAYMENT_INFO + "/{paymentID}")
    ResponseEntity<ObjectNode> getPaymentInfo(@PathVariable("paymentID") String paymentID) throws Exception {
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
    ResponseEntity<ObjectNode> getManualPaymentByID(@PathVariable("paymentID") String paymentID) throws Exception {
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

    @GetMapping(PaymentAPI.GET_PAYMENT_BY_USER_ID + "/{userID}")
    ResponseEntity<ObjectNode> getPaymentByUserID(@PathVariable("userID") String userID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.getPaymentByUserID(userID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.GET_ALL_PAYMENT)
    ResponseEntity<ObjectNode> getAllPayment() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.getAllPaymentResponse();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.CALCULATE_PAYMENT_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK)
    ResponseEntity<ObjectNode> calculatePaymentGrowthPercentageForCurrentAndPreviousWeek(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        Float growthPercentage = paymentService.calculatePaymentGrowthPercentageForCurrentAndPreviousWeek();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate New Customer Growth Percentage For Current and Previous Week Successfully");
        response.put("data", growthPercentage);
        return ResponseEntity.ok(response);
    }
}
