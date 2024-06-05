package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.BrandMaterialService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIConstant.BrandMaterialAPI.BRAND_MATERIAL)
@RequiredArgsConstructor
@Slf4j
public class BrandMaterialController {
    private final BrandMaterialService brandMaterialService;
    private final Logger logger = LoggerFactory.getLogger(BrandMaterialController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.BrandMaterialAPI.GET_ALL_BRAND_MATERIAL)
    public ResponseEntity<ObjectNode> getAllBrandMaterials() {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var materials = brandMaterialService.getAllBrandMaterial();
            if (!materials.isEmpty()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_BRAND_MATERIAL_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(materials));
            } else {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_BRAND_MATERIAL);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.BrandMaterialAPI.GET_ALL_BRAND_MATERIAL_BY_BRAND_NAME)
    public ResponseEntity<ObjectNode> getAllBrandMaterialsByBrandName(@RequestParam String brandName) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var materials = brandMaterialService.getAllBrandMaterialByBrandName(brandName);
            if (materials != null) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_BRAND_MATERIAL_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(materials));
            } else {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_BRAND);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(APIConstant.BrandMaterialAPI.ADD_NEW_BRAND_MATERIAL)
    public ResponseEntity<ObjectNode> addNewBrandMaterial(@RequestBody BrandMaterialRequest brandMaterialRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = brandMaterialService.createBrandMaterial(brandMaterialRequest);
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN CREATE MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}