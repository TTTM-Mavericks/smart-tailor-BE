package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.OrderAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.event.CreateOrderEvent;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.utils.request.OrderPickingRequest;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher applicationEventPublisher;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping(OrderAPI.CREATE_ORDER)
    public ResponseEntity<ObjectNode> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.CREATE_ORDER_SUCCESSFULLY);
            OrderResponse orderResponse = orderService.createOrder(orderRequest);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderByID(@ValidUUID @PathVariable("orderID") UUID orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderByOrderID(orderID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getOrderByBrandID(@ValidUUID @PathVariable("brandID") UUID brandID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderByBrandID(brandID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getOrderByUserID(@ValidUUID @PathVariable("userID") UUID userID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderByUserID(userID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ALL_ORDER)
    public ResponseEntity<ObjectNode> getAllOrder() {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getAllOrder();
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_PARENT_ORDER_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getParentOrderByDesignID(@ValidUUID @PathVariable("designID") UUID designID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getParentOrderByDesignID(designID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ALL_ORDER + "/{parentID}")
    public ResponseEntity<ObjectNode> getAllSubOrderByParentOrderID(@ValidUUID @PathVariable("parentID") UUID parentID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getSubOrderByParentID(parentID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @PutMapping(OrderAPI.CHANGE_STATUS_ORDER)
    public ResponseEntity<ObjectNode> chageOrderStatus(@Valid @RequestBody OrderStatusUpdateRequest orderRequest) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.CHANGE_ORDER_STATUS_SUCCESSFULLY);
            var orderResponse = orderService.changeOrderStatus(orderRequest);
            response.set("data", objectMapper.valueToTree(orderResponse));
            if (orderResponse.getOrderType().contains("PARENT_ORDER") &&
                    orderResponse.getOrderStatus().equals(OrderStatus.PENDING)) {
                applicationEventPublisher.publishEvent(new CreateOrderEvent(orderResponse));
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @PostMapping(OrderAPI.BRAND_PICK_ORDER)
    public ResponseEntity<ObjectNode> pickOrder(@RequestBody OrderPickingRequest orderPicking) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            if (orderPicking == null) {
                response.put("status", 400);
                response.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(response);
            }

            var brandPicked = orderService.brandPickOrder(orderPicking);
            response.put("status", 200);
            response.put("message", MessageConstant.BRAND_PICK_ORDER_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandPicked));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN BRAND PICKING ORDER. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_DETAIL_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderDetailByID(@ValidUUID @PathVariable("orderID") UUID orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderDetailByOrderID(orderID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_STAGE_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderStageByID(@ValidUUID @PathVariable("orderID") UUID orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_STAGE_SUCCESSFULLY);
            var orderResponse = orderService.getOrderStageByOrderID(orderID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping("/filter-brand-by-design-id/{designID}")
    public ResponseEntity<ObjectNode> filterBrandForSpecificOrderBaseOnDesign(@ValidUUID @PathVariable("designID") UUID designID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", "Filter Brand For Specific Order Base On Design");
            var listFilterBrand = orderService.filterBrandForSpecificOrderBaseOnDesign(designID);
            response.set("data", objectMapper.valueToTree(listFilterBrand));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }
}
