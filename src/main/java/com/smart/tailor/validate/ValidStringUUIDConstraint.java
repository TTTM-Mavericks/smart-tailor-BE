package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class ValidStringUUIDConstraint implements ConstraintValidator<ValidStringUUID, String> {
    @Override
    public void initialize(ValidStringUUID constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return false;
        try{
            UUID.fromString(value);
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
