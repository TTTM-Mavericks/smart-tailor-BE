package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.SampleProductDataAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.SampleProductDataService;
import com.smart.tailor.utils.request.SampleProductDataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(SampleProductDataAPI.SAMPLE_PRODUCT)
public class SampleProductDataController {
    private final SampleProductDataService dataService;

    @PostMapping(SampleProductDataAPI.ADD_SAMPLE_PRODUCT)
    public ResponseEntity<ObjectNode> addSampleProduct(@RequestBody SampleProductDataRequest sampleData) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var responseData = dataService.addNewSample(sampleData);
            response.put("status", 200);
            response.put("message", MessageConstant.ADD_SAMPLE_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(responseData));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(SampleProductDataAPI.UPDATE_SAMPLE_PRODUCT)
    public ResponseEntity<ObjectNode> updateSampleProduct(@RequestBody SampleProductDataRequest sampleData) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var responseData = dataService.updateSample(sampleData);
            response.put("status", 200);
            response.put("message", MessageConstant.UPDATE_SAMPLE_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(responseData));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_BY_ORDER_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getSampleProductByOrderID(@PathVariable("orderID") UUID orderID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var responseData = dataService.getByOrderID(orderID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_SAMPLE_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(responseData));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_BY_ID + "/{sampleID}")
    public ResponseEntity<ObjectNode> getSampleProductByID(@PathVariable("sampleID") UUID sampleID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var responseData = dataService.getByID(sampleID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_SAMPLE_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(responseData));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            return ResponseEntity.ok(response);
        }
    }
}
