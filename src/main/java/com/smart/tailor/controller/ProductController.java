package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.constant.APIConstant.ProductAPI;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.service.ProductService;
import com.smart.tailor.utils.request.ProductRequest;
import com.smart.tailor.utils.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(ProductAPI.Product)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping(ProductAPI.ADD_NEW_PRODUCT)
    ResponseEntity<ObjectNode> addNewProduct(@RequestBody ProductRequest productRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {

        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {
                CustomExeption customExeption = (CustomExeption) ex;
                response.put("status", customExeption.getErrorConstant().getStatusCode());
                response.put("message", customExeption.getErrorConstant().getMessage());
            } else {
                response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
                response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            }
            logger.error("ERROR IN PRODUCT CONTROLLER - ADD NEW PRODUCT. ERROR MESSAGE: {}", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(ProductAPI.GET_PRODUCT + "/{productID}")
    ResponseEntity<ObjectNode> getProduct(@PathVariable("productID") UUID productID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            ProductResponse productResponse = productService.getProduct(productID);
        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {
                CustomExeption customExeption = (CustomExeption) ex;
                response.put("status", customExeption.getErrorConstant().getStatusCode());
                response.put("message", customExeption.getErrorConstant().getMessage());
            } else {
                response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
                response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            }
            logger.error("ERROR IN PRODUCT CONTROLLER - GET ALL PRODUCT BY BRAND ID. ERROR MESSAGE: {}", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(ProductAPI.GET_ALL_PRODUCT_BY_BRAND_ID)
    ResponseEntity<ObjectNode> getAllProductByBrandID(@RequestParam(value = "brandID", required = true) UUID brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {

        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {
                CustomExeption customExeption = (CustomExeption) ex;
                response.put("status", customExeption.getErrorConstant().getStatusCode());
                response.put("message", customExeption.getErrorConstant().getMessage());
            } else {
                response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
                response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            }
            logger.error("ERROR IN PRODUCT CONTROLLER - GET ALL PRODUCT BY BRAND ID. ERROR MESSAGE: {}", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(ProductAPI.GET_ALL_PRODUCT_BY_BRAND_NAME)
    ResponseEntity<ObjectNode> getAllProductByBrandName(@RequestParam(value = "brandName", required = true) String brandName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {

        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {
                CustomExeption customExeption = (CustomExeption) ex;
                response.put("status", customExeption.getErrorConstant().getStatusCode());
                response.put("message", customExeption.getErrorConstant().getMessage());
            } else {
                response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
                response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            }
            logger.error("ERROR IN PRODUCT CONTROLLER - GET ALL PRODUCT BY BRAND NAME. ERROR MESSAGE: {}", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(ProductAPI.GET_ALL_PRODUCT_BY_DESIGN_ID)
    ResponseEntity<ObjectNode> getAllProductByDesignID(@RequestParam(value = "designID", required = true) UUID designID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {

        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {
                CustomExeption customExeption = (CustomExeption) ex;
                response.put("status", customExeption.getErrorConstant().getStatusCode());
                response.put("message", customExeption.getErrorConstant().getMessage());
            } else {
                response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
                response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            }
            logger.error("ERROR IN PRODUCT CONTROLLER - GET ALL PRODUCT BY DESIGN ID. ERROR MESSAGE: {}", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(ProductAPI.GET_ALL_PRODUCT_BY_USER_ID)
    ResponseEntity<ObjectNode> getAllProductByUserID(@RequestParam(value = "userID", required = true) UUID userID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {

        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {
                CustomExeption customExeption = (CustomExeption) ex;
                response.put("status", customExeption.getErrorConstant().getStatusCode());
                response.put("message", customExeption.getErrorConstant().getMessage());
            } else {
                response.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
                response.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            }
            logger.error("ERROR IN PRODUCT CONTROLLER - GET ALL PRODUCT BY USER ID. ERROR MESSAGE: {}", ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
