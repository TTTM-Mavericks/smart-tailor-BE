package com.smart.tailor.exception;


import org.springframework.http.HttpStatus;

public class ExternalServiceException extends RuntimeException{
    private final HttpStatus httpStatus;

    public ExternalServiceException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
