package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(APIConstant.DesignAPI.DESIGN)
@RequiredArgsConstructor
@Slf4j
public class DesignController {
    private final DesignService designService;
    private final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final ObjectMapper objectMapper;

    @PostMapping(APIConstant.DesignAPI.ADD_NEW_DESIGN)
    public ResponseEntity<ObjectNode> addNewDesign(@RequestBody DesignRequest designRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = designService.createDesign(designRequest);
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN ADD NEW DESIGN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllDesignByUserID(@PathVariable("userID") UUID userID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var designResponseList = designService.getAllDesignByUserID(userID);
            if(designResponseList.isEmpty()){
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN_BY_USER_ID);
            }else{
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(designResponseList));
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL DESIGN BY USER ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }


    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN)
    public ResponseEntity<ObjectNode> getAllDesign() {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var designResponseList = designService.getAllDesign();
            if(designResponseList.isEmpty()){
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
            }else{
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_DESIGN_BY_USER_ID_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(designResponseList));
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL DESIGN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.DesignAPI.GET_DESIGN_BY_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getDesignByID(@PathVariable("designID") UUID designID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var design = designService.getDesignResponseByID(designID);
            if(design == null){
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_DESIGN);
            }else{
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_DESIGN_BY_ID_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(design));
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET DESIGN BY DESIGN ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_CUSTOMER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllDesignByCustomerID(@PathVariable("userID") UUID userID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = designService.getAllDesignByUserIDAndRoleName(userID, RoleType.CUSTOMER.name());
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL DESIGN BY CUSTOMER ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.DesignAPI.GET_ALL_DESIGN_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllDesignByBrandID(@PathVariable("brandID") UUID brandID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = designService.getAllDesignByUserIDAndRoleName(brandID, RoleType.BRAND.name());
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL DESIGN BY BRAND ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(APIConstant.DesignAPI.UPDATE_PUBLIC_STATUS_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> updatePublicStatusByDesignID(@PathVariable("designID") UUID designID) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = designService.updatePublicStatusDesign(designID);
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL DESIGN BY BRAND ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
