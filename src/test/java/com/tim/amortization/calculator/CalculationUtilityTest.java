package com.tim.amortization.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.tim.amortization.calculator.actions.CalculationUtility;
import com.tim.amortization.calculator.exception.InputValidationException;
import com.tim.amortization.calculator.model.AmortizationRecord;

@TestInstance(Lifecycle.PER_CLASS)
public class CalculationUtilityTest {

	JTextField principalField;
	JTextField interestField;
	JTextField mortgageField;

	Double principalAmt = 603500.00;
	Double interestAmt = 6.5;
	Double mortgageLength = 30.00;

	@BeforeEach
	public void setup() {

		principalField = new JTextField();
		interestField = new JTextField();
		mortgageField = new JTextField();

		principalField.setText("603,500");
		interestField.setText("6.5");
		mortgageField.setText("30");
	}

	@Test
	public void calculateMonthlyPaymentTest() {
		BigDecimal result = CalculationUtility.calculateMonthlyPayment(principalAmt, interestAmt, mortgageLength);
		assertNotNull(result);
		assertEquals(new BigDecimal("3814.54"), result);
	}

	@Test
	public void calculateMonthlyInterestTest() {
		BigDecimal result = CalculationUtility.calculateMonthlyInterest(principalAmt, interestAmt);
		assertNotNull(result);
		assertEquals(new BigDecimal("3268.96"), result);

	}

	@Test
	public void invalidInputs() throws IOException {
		principalField.setText("abc");
		interestField.setText("abc");
		mortgageField.setText("abc");
		assertThrows(InputValidationException.class,
				() -> CalculationUtility.calculateAmortizationSchedule(principalField, interestField, mortgageField));
	}

	@Test
	public void nullInputs() throws IOException {
		principalField.setText(null);
		interestField.setText(null);
		mortgageField.setText(null);
		assertThrows(InputValidationException.class,
				() -> CalculationUtility.calculateAmortizationSchedule(principalField, interestField, mortgageField));
	}
	
	@Test
	public void successTest() throws IOException {
		List<AmortizationRecord> records = CalculationUtility.calculateAmortizationSchedule(principalField, interestField, mortgageField);
		
		assertNotNull(records);
		assertEquals(360, records.size());
		
		AmortizationRecord rec = records.get(0);
		assertEquals(1, rec.getMonth());
		assertEquals(545.58, rec.getPrincipalPaid().doubleValue());
		assertEquals(3268.96, rec.getInterestPaid().doubleValue());
		assertEquals(602954.43, rec.getRemainingPrincipal().doubleValue());

		System.out.println(records.get(0).toString());
	}
}
