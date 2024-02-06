package com.kernelsquare.core.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({ValidationGroups.NotBlankGroup.class, ValidationGroups.NotNullGroup.class,
	ValidationGroups.SizeGroup.class, ValidationGroups.PatternGroup.class, ValidationGroups.EmailGroup.class})
public interface ValidationSequence {
}
