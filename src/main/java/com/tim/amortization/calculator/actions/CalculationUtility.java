package com.tim.amortization.calculator.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

public class CalculationUtility {

	static Double principal = null;
	static Double interest = null;
	static Double mortgage = null;

	private static Double convertTextInput(String input) {
		if (!StringUtils.isEmpty(input)) {
			input = StringUtils.replace(input, ",", "");
			return Double.valueOf(input);
		} else {
			return null;
		}

	}

	/**
	 * Calculate monthly payment with a PMT function.
	 * 
	 * M = P( r(1+r)^n / (1 + r)^n - 1)
	 * 
	 * M = monthly payment 
	 * P = Principal Amount 
	 * r = monthly interest rate (Ex: 6.5% --> (6.5 / 100) / 12 = 0.0054167) 
	 * n = number of payments (Ex: 30-year --> 30 * 12 = 360)
	 * 
	 * @param principalAmt
	 * @param interestAmt
	 * @param mortgageLength
	 * @return
	 */
	public static BigDecimal calculateMonthlyPayment(Double principalAmt, Double interestAmt,
			Double mortgageLength) {

		// determine values for PMT function
		Double numberOfPayment = mortgageLength * 12; // number of months for mortgage
		Double rate = (interestAmt / 100) / 12;

		// calculate PMT function
		Double monthlyPayment = principalAmt * (rate * Math.pow(1 + rate, numberOfPayment))
				/ (Math.pow(1 + rate, numberOfPayment) - 1);

		// round the result to the nearest cent value
		return new BigDecimal(String.valueOf(monthlyPayment)).setScale(2, RoundingMode.CEILING);
	}

	/**
	 * Calculate the current month's interest fee
	 * 
	 * MI = (P * I) / 12
	 * 
	 * MI = monthly interest P = principal I * interest rate
	 * 
	 * @param principalAmt value of mortgage principal
	 * @param interestAmt  interest percentage for the mortgage
	 * @return
	 */
	public static BigDecimal calculateMonthlyInterest(Double principalAmt, Double interestAmt) {
		Double monthlyInterest = principalAmt * (interestAmt / 100) / 12;

		// round the result to the nearest cent value
		return new BigDecimal(monthlyInterest).setScale(2, RoundingMode.CEILING);
	}
	
	/**
	 * Container method to determine the monthly payments and amortization schedule
	 * 
	 * 
	 * @param principalField JTextField which contains value for principal value of
	 *                       mortgage
	 * @param interestField  JTextField which contains interest percentage
	 * @param mortgageField  JTextField which contains length (years) of mortgage
	 */
	public static void calculateAmortizationSchedule(JTextField principalField, JTextField interestField,
			JTextField mortgageField) {

		Double principalAmt = convertTextInput(principalField.getText());
		Double interestAmt = convertTextInput(interestField.getText());
		Double mortgageLength = convertTextInput(mortgageField.getText());

		BigDecimal monthlyPayment = CalculationUtility.calculateMonthlyPayment(principalAmt, interestAmt,
				mortgageLength);

		// determine values for PMT function
		Double numberOfPayments = mortgageLength * 12; // number of months for mortgage
		int counter = 1;

		for (double i = numberOfPayments; i > 0; i--) {
			BigDecimal monthlyInterest = CalculationUtility.calculateMonthlyInterest(principalAmt, interestAmt);
			Double principalOnly = monthlyPayment.doubleValue() - monthlyInterest.doubleValue();

			principalAmt -= principalOnly;
			System.out.println(MessageFormat.format(
					"Month[{0}]: Principal Payment: {1} \t Interest Payment: {2} \t Remaining Principal: {3}", counter,
					new BigDecimal(principalOnly).setScale(2, RoundingMode.CEILING).toPlainString(),
					monthlyInterest.toPlainString(), new BigDecimal(principalAmt).setScale(2, RoundingMode.CEILING).toPlainString()));
			counter++;
		}
	}

}
