package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(APIConstant.DesignAPI.DESIGN)
@RequiredArgsConstructor
@Slf4j
@Validated
public class DesignController {
    private final DesignService designService;
    private final Logger logger = LoggerFactory.getLogger(DesignController.class);
    private final ObjectMapper objectMapper;

    @PostMapping(APIConstant.DesignAPI.ADD_NEW_DESIGN)
    public ResponseEntity<ObjectNode> addNewDesign(@Valid @RequestBody DesignRequest designRequest) {
        var apiResponse = designService.createDesign(designRequest);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllDesignByUserID(@ValidUUID @PathVariable("userID") UUID userID) {
        var designResponseList = designService.getAllDesignByUserID(userID);
        ObjectNode response = objectMapper.createObjectNode();
        if (designResponseList.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN_BY_USER_ID);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(designResponseList));
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN)
    public ResponseEntity<ObjectNode> getAllDesign() {
        var designResponseList = designService.getAllDesign();
        ObjectNode response = objectMapper.createObjectNode();
        if (designResponseList.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(designResponseList));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_DESIGN_BY_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getDesignByID(@ValidUUID @PathVariable("designID") UUID designID) {
        var design = designService.getDesignResponseByID(designID);
        ObjectNode response = objectMapper.createObjectNode();
        if (design == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_DESIGN_BY_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(design));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_CUSTOMER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllDesignByCustomerID(@ValidUUID @PathVariable("userID") UUID userID) {
        var apiResponse = designService.getAllDesignByUserIDAndRoleName(userID, RoleType.CUSTOMER.name());
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllDesignByBrandID(@ValidUUID @PathVariable("brandID") UUID brandID) {
        var apiResponse = designService.getAllDesignByUserIDAndRoleName(brandID, RoleType.BRAND.name());
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.DesignAPI.UPDATE_PUBLIC_STATUS_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> updatePublicStatusByDesignID(@ValidUUID @PathVariable("designID") UUID designID) {
        var apiResponse = designService.updatePublicStatusDesign(designID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }
}
