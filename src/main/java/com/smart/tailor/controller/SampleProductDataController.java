package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.SampleProductDataAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.SampleProductDataService;
import com.smart.tailor.utils.request.SampleProductDataRequest;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(SampleProductDataAPI.SAMPLE_PRODUCT_DATA)
@Validated
public class SampleProductDataController {
    private final SampleProductDataService dataService;

    @PostMapping(SampleProductDataAPI.ADD_SAMPLE_PRODUCT_DATA)
    public ResponseEntity<ObjectNode> addSampleProduct(@Valid @RequestBody SampleProductDataRequest sampleData) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        dataService.addNewSampleProductData(sampleData);
        response.put("status", 200);
        response.put("message", MessageConstant.ADD_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(SampleProductDataAPI.UPDATE_SAMPLE_PRODUCT_DATA + "/{sampleModelID}")
    public ResponseEntity<ObjectNode> updateSampleProduct(@ValidUUID @PathVariable("sampleModelID") UUID sampleModelID,
                                                          @Valid @RequestBody SampleProductDataRequest sampleData) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        dataService.updateSampleProductData(sampleModelID, sampleData);
        response.put("status", 200);
        response.put("message", MessageConstant.UPDATE_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_DATA_BY_ORDER_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getSampleProductByOrderID(@ValidUUID @PathVariable("orderID") UUID orderID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var responseData = dataService.getSampleProductDataByOrderID(orderID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(responseData));
        return ResponseEntity.ok(response);
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_DATA_BY_ID + "/{sampleModelID}")
    public ResponseEntity<ObjectNode> getSampleProductByID(@ValidUUID @PathVariable("sampleModelID") UUID sampleModelID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var responseData = dataService.getSampleProductDataByID(sampleModelID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(responseData));
        return ResponseEntity.ok(response);
    }
}
