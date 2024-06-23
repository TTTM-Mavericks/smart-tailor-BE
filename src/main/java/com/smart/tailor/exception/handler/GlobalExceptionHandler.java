package com.smart.tailor.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ExternalServiceException;
import com.smart.tailor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ObjectNode> handleBadRequestException(BadRequestException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ObjectNode> handleNotFoundException(ResourceNotFoundException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ObjectNode> handleExternalServiceException(ExternalServiceException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", ex.getHttpStatus().value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ObjectNode> handleGeneralException(Exception ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ObjectNode> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        String unsupportedMethod = ex.getMethod();
        List<String> supportedMethod = new ArrayList<>();
        for(HttpMethod requestMethod : ex.getSupportedHttpMethods()){
            supportedMethod.add(requestMethod.name());
        }
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        response.put("message", "Method " + unsupportedMethod + " is not supported for this endpoint.");
        response.set("supportedMethod", objectMapper.valueToTree(supportedMethod));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ObjectNode> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Malformed JSON request");
        response.put("errors", ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
