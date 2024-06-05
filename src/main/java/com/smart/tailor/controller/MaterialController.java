package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.MaterialResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIConstant.MaterialAPI.MATERIAL)
@RequiredArgsConstructor
@Slf4j
public class MaterialController {
    private final MaterialService materialService;
    private final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.MaterialAPI.GET_ALL_MATERIAL)
    public ResponseEntity<ObjectNode> getAllMaterials() {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var materials = materialService.findAllMaterials();
            if (!materials.isEmpty()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_MATERIAL_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(materials));
            } else {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(APIConstant.MaterialAPI.ADD_NEW_MATERIAL)
    public ResponseEntity<ObjectNode> addNewMaterial(@RequestBody MaterialRequest materialRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            if(!Utilities.isNonNullOrEmpty(materialRequest.getMaterialName()) ||  !Utilities.isNonNullOrEmpty(materialRequest.getCategoryName())){
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", MessageConstant.MISSING_ARGUMENT);
            }
            var material = materialService.createMaterial(materialRequest.getMaterialName(), materialRequest.getCategoryName());
            logger.info("Material Response {}", material);
            if (Optional.ofNullable(material).isPresent()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.ADD_NEW_MATERIAL_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(material));
            } else {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", MessageConstant.MATERIAL_IS_EXISTED);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN CREATE MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
