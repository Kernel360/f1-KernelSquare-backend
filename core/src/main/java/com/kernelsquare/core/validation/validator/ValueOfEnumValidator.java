package com.kernelsquare.core.validation.validator;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kernelsquare.core.validation.annotations.EnumValue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

	private Set<String> acceptedValues;

	@Override
	public void initialize(EnumValue constraintAnnotation) {
		acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
			.map(this::getDescription)
			.collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (Objects.isNull(value)) {
			return true;
		}

		return acceptedValues.contains(value);
	}

	private String getDescription(Enum<?> enumValue) {
		try {
			Field descriptionField = enumValue.getClass().getDeclaredField("description");
			descriptionField.setAccessible(true);
			return (String)descriptionField.get(enumValue);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			return enumValue.name();
		}
	}
}
