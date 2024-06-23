package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDataTypeConstraint implements ConstraintValidator<ValidDataType, Object> {
    @Override
    public void initialize(ValidDataType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // or false, depending on your validation requirements for null values
        }

        if (value instanceof Boolean) {
            return true; // Boolean values are always valid
        } else if (value instanceof Double) {
            return true; // Double values are always valid
        } else if (value instanceof Float) {
            return true; // Float values are always valid
        } else if (value instanceof Integer) {
            return true; // Integer values are always valid
        }

        // If the value is not of the expected types, return false
        return false;
    }
}
