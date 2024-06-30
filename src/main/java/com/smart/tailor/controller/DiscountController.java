package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.DiscountAPI;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.DiscountService;
import com.smart.tailor.utils.request.DiscountRequest;
import com.smart.tailor.utils.response.DiscountResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DiscountAPI.DISCOUNT)
@RequiredArgsConstructor
public class DiscountController {
    private final Logger logger = LoggerFactory.getLogger(DiscountController.class);
    private final DiscountService discountService;

    @GetMapping(DiscountAPI.GET_ALL_DISCOUNT)
    public ResponseEntity<ObjectNode> getAllDiscount() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            List<DiscountResponse> responseList = discountService.getAllDiscount();
            respon.put("status", 200);
            respon.put("message", MessageConstant.GET_ALL_DISCOUNT_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(responseList));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN GET ALL DISCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(DiscountAPI.GET_ALL_VALID_DISCOUNT)
    public ResponseEntity<ObjectNode> getAllValidDiscount() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            List<DiscountResponse> responseList = discountService.getAllValidDiscount();
            respon.put("status", 200);
            respon.put("message", MessageConstant.GET_ALL_VALID_DISCOUNT_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(responseList));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN GET ALL VALID DISCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(DiscountAPI.ADD_NEW_DISCOUNT)
    public ResponseEntity<ObjectNode> addNewDiscount(@RequestBody DiscountRequest discountRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {

            discountService.addNewDiscount(discountRequest);

            response.put("status", 200);
            response.put("message", MessageConstant.ADD_NEW_DISCOUNT_SUCCESSFULLY);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN ADD NEW DISCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
