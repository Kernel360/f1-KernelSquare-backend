package com.kernelsquare.core.validation.validator;

import java.util.List;

import com.kernelsquare.core.validation.annotations.BadWordFilter;
import com.vane.badwordfiltering.BadWordFiltering;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BadWordValidator implements ConstraintValidator<BadWordFilter, Object> {
	private BadWordFiltering badWordFiltering;
	@Override
	public void initialize(BadWordFilter constraintAnnotation) {
		badWordFiltering = new BadWordFiltering();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return switch (value) {
			case String str -> !badWordFiltering.check(str);
			case List<?> list -> handleStringList((List<?>) list);
			default -> true;
		};
	}

	private boolean handleStringList(List<?> list) {
		return list.stream()
			.map(String.class::cast)
			.noneMatch(badWordFiltering::check);
	}
}
