package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.utils.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(APIConstant.CategoryAPI.CATEGORY)
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping(APIConstant.CategoryAPI.GET_ALL_CATEGORY)
    public ResponseEntity<ObjectNode> getAllCategories() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var listRole = categoryService.findAllCatgories();
            if (!listRole.isEmpty()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.GET_ALL_CATEGORY_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(listRole));
            } else {
                response.put("status", HttpStatus.OK.value());
                response.put("message", MessageConstant.CAN_NOT_FIND_ANY_CATEGORY);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET CATEGORY BY ID. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(APIConstant.CategoryAPI.GET_CATEGORY_BY_ID + "/{categoryID}")
    public ResponseEntity<ObjectNode> getCategoryByID(@PathVariable("categoryID") UUID categoryID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = categoryService.findCategoryByID(categoryID);
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL CATEGORIES. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(APIConstant.CategoryAPI.ADD_NEW_CATEGORY)
    public ResponseEntity<ObjectNode> addNewCategory(@RequestParam("categoryName") String categoryName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = categoryService.createCategory(categoryName);
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN ADD NEW CATEGORY. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(APIConstant.CategoryAPI.UPDATE_CATEGORY)
    public ResponseEntity<ObjectNode> updateCategory(@RequestBody CategoryRequest categoryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var apiResponse = categoryService.updateCategory(categoryRequest);
            response.put("status", apiResponse.getStatus());
            response.put("message", apiResponse.getMessage());
            response.set("data", objectMapper.valueToTree(apiResponse.getData()));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN UPDATE CATEGORY. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

}
