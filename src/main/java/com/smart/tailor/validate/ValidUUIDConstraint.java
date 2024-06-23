package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class ValidUUIDConstraint implements ConstraintValidator<ValidUUID, UUID> {
    @Override
    public void initialize(ValidUUID constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if(value == null) return false;
        try{
            UUID.fromString(value.toString());
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
