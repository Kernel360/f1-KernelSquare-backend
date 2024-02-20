package com.kernelsquare.core.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

	private EnumValue enumValue;

	@Override
	public void initialize(EnumValue constraintAnnotation) {
		this.enumValue = constraintAnnotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result = false;
		Enum<?>[] enumValues = this.enumValue.enumClass().getEnumConstants();
		if (enumValues != null) {
			for (Object enumValue : enumValues) {
				if (value.equals(enumValue.toString())
					|| this.enumValue.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
}
