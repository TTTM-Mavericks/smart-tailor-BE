package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.utils.request.CategoryRequest;
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
@RequestMapping(APIConstant.CategoryAPI.CATEGORY)
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping(APIConstant.CategoryAPI.GET_ALL_CATEGORY)
    public ResponseEntity<ObjectNode> getAllCategories() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var listCategory = categoryService.findAllCatgories();
        if (!listCategory.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_CATEGORY_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(listCategory));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_CATEGORY);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.CategoryAPI.GET_CATEGORY_BY_ID + "/{categoryID}")
    public ResponseEntity<ObjectNode> getCategoryByID(@ValidUUID @PathVariable("categoryID") UUID categoryID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var category = categoryService.findCategoryByID(categoryID);
        if (category != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_CATEGORY_BY_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(category));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_CATEGORY);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.CategoryAPI.ADD_NEW_CATEGORY)
    public ResponseEntity<ObjectNode> addNewCategory(@RequestParam(value = "categoryName") String categoryName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = categoryService.createCategory(categoryName);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.CategoryAPI.UPDATE_CATEGORY)
    public ResponseEntity<ObjectNode> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = categoryService.updateCategory(categoryRequest);
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }
}
