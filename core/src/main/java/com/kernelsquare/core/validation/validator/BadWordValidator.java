package com.kernelsquare.core.validation.validator;

import com.kernelsquare.core.validation.annotations.BadWordFilter;
import com.vane.badwordfiltering.BadWordFiltering;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BadWordValidator implements ConstraintValidator<BadWordFilter, String> {
	private BadWordFiltering badWordFiltering;
	@Override
	public void initialize(BadWordFilter constraintAnnotation) {
		badWordFiltering = new BadWordFiltering();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !badWordFiltering.check(value);
	}
}
