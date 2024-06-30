package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.validate.ValidUUID;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(APIConstant.MaterialAPI.MATERIAL)
@RequiredArgsConstructor
@Slf4j
@Validated
public class MaterialController {
    private final MaterialService materialService;
    private final Logger logger = LoggerFactory.getLogger(MaterialController.class);
    private final ObjectMapper objectMapper;

    @GetMapping(APIConstant.MaterialAPI.GET_ALL_MATERIAL)
    public ResponseEntity<ObjectNode> getAllMaterials() {
        ObjectNode response = objectMapper.createObjectNode();
        var materials = materialService.findAllMaterials();
        if (!materials.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_MATERIAL_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(materials));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.MaterialAPI.GET_ALL_ACTIVE_MATERIAL)
    public ResponseEntity<ObjectNode> getAllActiveMaterials() {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var materials = materialService.findAllActiveMaterials();
            if (!materials.isEmpty()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_MATERIAL_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(materials));
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL ACTIVE MATERIALS. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.MaterialAPI.GET_MATERIAL_BY_ID + "/{materialID}")
    public ResponseEntity<ObjectNode> getMaterialByID(@ValidUUID @PathVariable("materialID") UUID materialID) {
        ObjectNode response = objectMapper.createObjectNode();
        var materials = materialService.findByMaterialID(materialID);
        if (materials != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_MATERIAL_BY_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(materials));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.MaterialAPI.UPDATE_STATUS_MATERIAL + "/{materialID}")
    public ResponseEntity<ObjectNode> changeStatusMaterial(@ValidUUID @PathVariable("materialID") UUID materialID) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = materialService.updateStatusMaterial(materialID);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.MaterialAPI.UPDATE_MATERIAL + "/{materialID}")
    public ResponseEntity<ObjectNode> updateMaterial(@ValidUUID  @PathVariable("materialID") UUID materialID,
                                                     @Valid @RequestBody MaterialRequest materialRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = materialService.updateMaterial(materialID, materialRequest);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }


    @PostMapping(APIConstant.MaterialAPI.ADD_NEW_MATERIAL)
    public ResponseEntity<ObjectNode> addNewMaterial(@Valid @RequestBody MaterialRequest materialRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = materialService.createMaterial(materialRequest);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.MaterialAPI.ADD_NEW_CATEGORY_MATERIAL_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> addNewMaterialByExcelFile(@RequestParam("file") MultipartFile file) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = materialService.createMaterialByExcelFile(file);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.MaterialAPI.EXPORT_CATEGORY_MATERIAL_FOR_BRAND_BY_EXCEL)
    public ResponseEntity<ObjectNode> exportCategoryMaterialForBrandByExcel(HttpServletResponse httpServletResponse) throws IOException {
        ObjectNode response = objectMapper.createObjectNode();
        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = Export_Category_Material_For_Brand.xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);
        var materials = materialService.exportCategoryMaterialForBrandByExcel(httpServletResponse);
        if (!materials.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.EXPORT_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(materials));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.MaterialAPI.GENERATE_SAMPLE_CATEGORY_MATERIAL_BY_EXCEL_FILE)
    public ResponseEntity<ObjectNode> generateSampleCategoryMaterialByExcelFile(HttpServletResponse httpServletResponse) throws IOException {
        ObjectNode response = objectMapper.createObjectNode();
        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = Generate_Sample_Category_Material.xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);
        materialService.generateSampleCategoryMaterialByExportExcel(httpServletResponse);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GENERATE_SAMPLE_CATEGORY_MATERIAL_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
