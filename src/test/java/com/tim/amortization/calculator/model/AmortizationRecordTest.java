package com.tim.amortization.calculator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class AmortizationRecordTest {

	private final static BigDecimal TEST_VALUE = new BigDecimal(25.32123123123).setScale(2, RoundingMode.CEILING);

	@Test
	public void getterSetterTest() {
		AmortizationRecord rec = new AmortizationRecord();

		rec.setMonth(1);
		rec.setPrincipalPaid(TEST_VALUE);
		rec.setInterestPaid(TEST_VALUE);
		rec.setRemainingPrincipal(TEST_VALUE);

		assertEquals(1, rec.getMonth());
		assertEquals(TEST_VALUE, rec.getPrincipalPaid());
		assertEquals(TEST_VALUE, rec.getInterestPaid());
		assertEquals(TEST_VALUE, rec.getRemainingPrincipal());

	}

	@Test
	public void hashCodeTest() {
		AmortizationRecord rec = new AmortizationRecord(1, TEST_VALUE, TEST_VALUE, TEST_VALUE);

		assertNotNull(rec.hashCode());
	}

	@Test
	public void equalsTest() {
		AmortizationRecord rec1 = new AmortizationRecord(1, TEST_VALUE, TEST_VALUE, TEST_VALUE);

		AmortizationRecord rec2 = new AmortizationRecord(1, TEST_VALUE, TEST_VALUE, TEST_VALUE);
		Object obj = new Object();

		assertTrue(rec1.equals(rec1));
		assertTrue(rec1.equals(rec2));
		assertFalse(rec1.equals(null));
		assertFalse(rec1.equals(obj));

		rec2.setMonth(12);
		assertFalse(rec1.equals(rec2));

	}

	@Test
	public void toStringTest() {
		AmortizationRecord rec = new AmortizationRecord(1, TEST_VALUE, TEST_VALUE, TEST_VALUE);

		assertEquals("AmortizationRecord [month=1, principalPaid=25.33, interestPaid=25.33, remainingPrincipal=25.33]",
				rec.toString());

	}
}
