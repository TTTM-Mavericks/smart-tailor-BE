package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.ReportService;
import com.smart.tailor.utils.request.ReportRequest;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstant.ReportAPI.REPORT)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ReportController {
    private final ReportService reportService;
    private final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @GetMapping(APIConstant.ReportAPI.GET_ALL_REPORT)
    public ResponseEntity<ObjectNode> getAllReport() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var reportResponses = reportService.getAllReport();
        if (!reportResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_REPORT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(reportResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_REPORT);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ReportAPI.GET_ALL_REPORT_BY_ORDER_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getAllReportByOrderID( @PathVariable("orderID") String orderID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var reportResponses = reportService.getAllReportByOrderID(orderID);
        if (!reportResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_REPORT_BY_ORDER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(reportResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_REPORT);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ReportAPI.GET_ALL_REPORT_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getAllReportByUserID( @PathVariable("userID") String userID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var reportResponses = reportService.getAllReportByUserID(userID);
        if (!reportResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_REPORT_BY_USER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(reportResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_REPORT);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ReportAPI.GET_ALL_REPORT_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllReportByBrandID( @PathVariable("brandID") String brandID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var reportResponses = reportService.getAllReportByBrandID(brandID);
        if (!reportResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_REPORT_BY_BRAND_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(reportResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_REPORT);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.ReportAPI.CREATE_REPORT)
    public ResponseEntity<ObjectNode> createReport(@Valid @RequestBody ReportRequest reportRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        reportService.createReport(reportRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.CREATE_REPORT_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.ReportAPI.GET_ALL_REPORT_BY_PARENT_ORDER_ID + "/{parentOrderID}")
    public ResponseEntity<ObjectNode> getAllReportByParentOrderID(@PathVariable("parentOrderID") String parentOrderID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var reportResponses = reportService.getAllReportByParentOrderID(parentOrderID);
        if (!reportResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_REPORT_BY_PARENT_ORDER_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(reportResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_REPORT);
        }
        return ResponseEntity.ok(response);
    }
}
