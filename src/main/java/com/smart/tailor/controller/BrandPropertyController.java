package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.BrandPropertyAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.BrandPropertiesService;
import com.smart.tailor.utils.request.BrandPropertiesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(BrandPropertyAPI.BRAND_PROPERTY)
public class BrandPropertyController {
    private final BrandPropertiesService brandPropertiesService;

    @GetMapping(BrandPropertyAPI.GET_ALL_BRAND_PROPERTY)
    public ResponseEntity<ObjectNode> getAllBrandProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getAll();
        response.put("status", 200);
        response.put("message", MessageConstant.GET_ALL_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandPropertyAPI.GET_ALL_BRAND_PROPERTY_BY_BRAND_ID)
    public ResponseEntity<ObjectNode> getAllByBrandID(@RequestParam("brandID") UUID brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getAllByBrandID(brandID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandPropertyAPI.GET_BRAND_PROPERTY + "/{systemID}")
    public ResponseEntity<ObjectNode> getByID(@PathVariable("systemID") UUID systemID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getByID(systemID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @PostMapping(BrandPropertyAPI.ADD_NEW_BRAND_PROPERTY)
    public ResponseEntity<ObjectNode> addNew(@RequestBody BrandPropertiesRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.addNew(request);
        response.put("status", 200);
        response.put("message", MessageConstant.ADD_NEW_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }
}
