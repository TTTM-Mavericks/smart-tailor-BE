package com.smart.tailor.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;

@Setter
@Getter
public class ExcelFileInvalidDataTypeException extends RuntimeException{
    private Object errorFields;
    public ExcelFileInvalidDataTypeException(String message, Object errorFields){
        super(message);
        this.errorFields = errorFields;
    }
}
