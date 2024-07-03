package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.DesignDetailAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.DesignDetailService;
import com.smart.tailor.utils.request.DesignDetailRequest;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(DesignDetailAPI.DESIGN_DETAIL)
@RequiredArgsConstructor
@Validated
public class DesignDetailController {
    private final Logger logger = LoggerFactory.getLogger(DesignDetailController.class);
    private final ObjectMapper objectMapper;
    private final DesignDetailService designDetailService;

    @PostMapping(DesignDetailAPI.ADD_NEW_DESIGN_DETAIL)
    public ResponseEntity<ObjectNode> addNewDesignDetail(@Valid @RequestBody DesignDetailRequest designDetailRequest) {
        var apiResponse = designDetailService.createDesignDetail(designDetailRequest);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(DesignDetailAPI.GET_ALL_DESIGN_DETAIL_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getAllDesignDetailByDesignID(@ValidUUID @PathVariable("designID") UUID designID) {
        var designDetailResponseList = designDetailService.getAllByDesignID(designID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GET_ALL_DESIGN_DETAIL_BY_DESIGN_ID_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(designDetailResponseList));
        return ResponseEntity.ok(response);
    }

}
