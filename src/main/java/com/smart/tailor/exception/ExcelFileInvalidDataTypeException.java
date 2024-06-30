package com.smart.tailor.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExcelFileInvalidDataTypeException extends RuntimeException{
    private List<Object> errors;
    public ExcelFileInvalidDataTypeException(String message, List<Object> errors){
        super(message);
        this.errors = errors;
    }
}