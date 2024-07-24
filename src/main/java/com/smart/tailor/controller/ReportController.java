package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.ReportService;
import com.smart.tailor.utils.request.ReportRequest;
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
    public ResponseEntity<ObjectNode> getAllReportByOrderID(@ValidUUID @PathVariable("orderID") UUID orderID) {
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

    @PostMapping(APIConstant.ReportAPI.CREATE_REPORT)
    public ResponseEntity<ObjectNode> createReport(@Valid @RequestBody ReportRequest reportRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        reportService.createReport(reportRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.CREATE_REPORT_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
