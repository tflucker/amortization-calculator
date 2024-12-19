package com.tim.amortization.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.tim.amortization.calculator.actions.CalculationUtility;

@TestInstance(Lifecycle.PER_CLASS)
public class CalculationUtilityTest {

	JTextField principalField;
	JTextField interestField;
	JTextField mortgageField;
	
	Double principalAmt = 603500.00;
	Double interestAmt = 6.5;
	Double mortgageLength = 30.00;


	@BeforeAll
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
}
