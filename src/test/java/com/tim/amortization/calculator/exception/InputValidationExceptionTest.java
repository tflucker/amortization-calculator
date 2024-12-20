package com.tim.amortization.calculator.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class InputValidationExceptionTest {

	@Test
	public void exceptionTest() {
		InputValidationException ex = new InputValidationException("This is a test");
		
		assertNotNull(ex);
		assertEquals("This is a test", ex.getMessage());
		
		ex.setMessage("Another message");
		assertEquals("Another message", ex.getMessage());

	}
}
