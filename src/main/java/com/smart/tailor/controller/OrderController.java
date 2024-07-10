package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.OrderAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.utils.request.OrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(OrderAPI.ORDER)
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping(OrderAPI.CREATE_ORDER)
    public ResponseEntity<ObjectNode> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.CREATE_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.createOrder(orderRequest);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderByID(@Valid @PathVariable("orderID") UUID orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.CREATE_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderByOrderID(orderID);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }
}
