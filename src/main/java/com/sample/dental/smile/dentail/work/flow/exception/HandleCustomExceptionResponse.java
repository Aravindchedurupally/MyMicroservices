package com.sample.dental.smile.dentail.work.flow.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

public class HandleCustomExceptionResponse {

	public Object handleValidationExceptionResponse(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

		// Map to extract the interpolated message and property path
		Set<String> errorMessages = violations.stream()
				.map(violation -> "Property: " + violation.getPropertyPath() + " - Message: " + violation.getMessage())
				.collect(Collectors.toSet());

		// Print or log the simplified error messages
		errorMessages.forEach(System.out::println);
		return errorMessages;

		
	}

}
