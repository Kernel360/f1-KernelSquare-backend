package com.kernelsquare.core.validation.validator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

	private List<String> acceptedValues;

	@Override
	public void initialize(EnumValue constraintAnnotation) {
		acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
			.map(this::getDescription)
			.toList();

		System.out.println(acceptedValues);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		return acceptedValues.contains(value.toString());
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
