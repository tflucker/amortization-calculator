package com.tim.amortization.calculator.utilities;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import com.tim.amortization.calculator.exception.InputValidationException;
import com.tim.amortization.calculator.model.AmortizationRecord;

/**
 * Contains logic to calculate payments and to output an excel document with all
 * the amortization data.
 */
public class CalculationUtility {

	/**
	 * Used to replace any commas in the input to allow for easier conversion into
	 * Double / BigDecimal
	 * 
	 * @param input
	 * @return
	 */
	private static Double convertTextInput(String input) {
		return Double.valueOf(StringUtils.replace(input, ",", ""));
	}

	private static void validateInput(JTextField principalField, JTextField interestField, JTextField mortgageField, JTextField additionalPrincipalField) {
		List<String> errorMessages = new ArrayList<>();

		String principalAmt = principalField.getText();
		String interestAmt = interestField.getText();
		String mortgageLength = mortgageField.getText();
		String additionalPrincipal = additionalPrincipalField.getText();

		if (StringUtils.isEmpty(principalAmt) || !Pattern.matches("(([0-9]){1,3}(,){1})+([0-9]){1,3}", principalAmt)) {
			errorMessages.add("Invalid value for Principal Amount field. Value must be numeric.");
		}
		if (StringUtils.isEmpty(interestAmt) || !Pattern.matches("([0-9]){1,2}(.){1}([0-9]){1,2}", interestAmt)) {
			errorMessages.add("Invalid value for Interest Percentage. must be numeric.");
		}
		if (StringUtils.isEmpty(mortgageLength) || !StringUtils.isNumeric(mortgageLength)) {
			errorMessages.add("Invalid value for Mortgage Length. Value must be numeric.");
		}
		if(StringUtils.isEmpty(additionalPrincipal) || !Pattern.matches("([0-9]){1,8}(.){1}([0-9]){1,2}", additionalPrincipal)) {
			errorMessages.add("Invalid value for Additional Principal. Value must be numeric.");
		}

		// if errorMessages is not empty then create one string with line separators to
		// return to the user
		if (!errorMessages.isEmpty()) {
			String err = errorMessages.stream().collect(Collectors.joining("\n"));
			throw new InputValidationException(err);
		}
	}

	/**
	 * Calculate monthly payment with a PMT function.
	 * 
	 * M = P( r(1+r)^n / (1 + r)^n - 1)
	 * 
	 * M = monthly payment P = Principal Amount r = monthly interest rate (Ex: 6.5%
	 * --> (6.5 / 100) / 12 = 0.0054167) n = number of payments (Ex: 30-year --> 30
	 * * 12 = 360)
	 * 
	 * @param principalAmt
	 * @param interestAmt
	 * @param mortgageLength
	 * @return
	 */
	public static BigDecimal calculateMonthlyPayment(Double principalAmt, Double interestAmt, Double mortgageLength) {

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
	 * @throws IOException
	 */
	public static List<AmortizationRecord> calculateAmortizationSchedule(JTextField principalField,
			JTextField interestField, JTextField mortgageField, JTextField additionalPrincipalField) throws IOException {

		// validate all user inputs, return String with a list of errors if validations
		// fail.
		validateInput(principalField, interestField, mortgageField, additionalPrincipalField);

		Double principalAmt = convertTextInput(principalField.getText());
		Double interestAmt = convertTextInput(interestField.getText());
		Double mortgageLength = convertTextInput(mortgageField.getText());
		Double additionalPrincipalPayment = convertTextInput(additionalPrincipalField.getText());

		// monthly payment
		BigDecimal monthlyPayment = CalculationUtility.calculateMonthlyPayment(principalAmt, interestAmt,
				mortgageLength);
		List<AmortizationRecord> records = new ArrayList<>();

		// determine values for PMT function
		Double numberOfPayments = mortgageLength * 12; // number of months for mortgage
		int counter = 1;

		// for every month of payment, calculate monthly interest. then fill in data for
		// excel sheet
		for (double i = numberOfPayments; i > 0; i--) {
			BigDecimal monthlyInterest = CalculationUtility.calculateMonthlyInterest(principalAmt, interestAmt);
			Double principalOnly = monthlyPayment.doubleValue() - monthlyInterest.doubleValue();

			// update principalAmt by subtracting the principalOnly amount and any additionalPrincipalPayment paid each month
			principalAmt -= (principalOnly + additionalPrincipalPayment);

			records.add(new AmortizationRecord(counter, new BigDecimal(principalOnly).setScale(2, RoundingMode.CEILING),
					monthlyInterest, new BigDecimal(principalAmt).setScale(2, RoundingMode.CEILING), new BigDecimal(additionalPrincipalPayment).setScale(2, RoundingMode.CEILING)));

			// increment counter for loop and month column in spreadsheet
			counter++;
		}

		return records;
	}

}
