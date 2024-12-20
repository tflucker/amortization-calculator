package com.tim.amortization.calculator.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tim.amortization.calculator.exception.InputValidationException;

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
		if (!StringUtils.isEmpty(input)) {
			input = StringUtils.replace(input, ",", "");
			return Double.valueOf(input);
		} else {
			return null;
		}
	}

	private static void validateInput(JTextField principalField, JTextField interestField,
			JTextField mortgageField) {
		List<String> errorMessages = new ArrayList<>();
		
		String principalAmt = principalField.getText();
		String interestAmt = interestField.getText();
		String mortgageLength = mortgageField.getText();
		
		if(StringUtils.isEmpty(principalAmt) || !Pattern.matches("(([0-9]){1,3}(,){1})+([0-9]){1,3}", principalAmt)) {
			errorMessages.add("Invalid value for Principal Amount field. Value must be numeric.");
		} 
		if(StringUtils.isEmpty(interestAmt) || !Pattern.matches("([0-9]){1,2}(.){1}([0-9]){1,2}", interestAmt)) {
			errorMessages.add("Invalid value for Interest Percentage. must be numeric");
		} 
		if(StringUtils.isEmpty(mortgageLength) || !StringUtils.isNumeric(mortgageLength)) {
			errorMessages.add("Invalid value for Mortgage Length. Value must be numeric");
		}
		
		
		// if errorMessages is not empty then create one string with line separators to return to the user
		if(!errorMessages.isEmpty()) {
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
	 */
	public static void calculateAmortizationSchedule(JTextField principalField, JTextField interestField,
			JTextField mortgageField) {

		// validate all user inputs, return String with a list of errors if validations fail.
		validateInput(principalField, interestField, mortgageField);
		
		Double principalAmt = convertTextInput(principalField.getText());
		Double interestAmt = convertTextInput(interestField.getText());
		Double mortgageLength = convertTextInput(mortgageField.getText());

		// monthly payment
		BigDecimal monthlyPayment = CalculationUtility.calculateMonthlyPayment(principalAmt, interestAmt,
				mortgageLength);
		Workbook workbook;
		try {
			// create Excel document
			workbook = createExcelDoc();

			// determine values for PMT function
			Double numberOfPayments = mortgageLength * 12; // number of months for mortgage
			int counter = 1;

			// for every month of payment, calculate monthly interest. then fill in data for
			// excel sheet
			for (double i = numberOfPayments; i > 0; i--) {
				BigDecimal monthlyInterest = CalculationUtility.calculateMonthlyInterest(principalAmt, interestAmt);
				Double principalOnly = monthlyPayment.doubleValue() - monthlyInterest.doubleValue();

				// update principalAmt by subtracting the principalOnly amount paid each month
				principalAmt -= principalOnly;

				CellStyle style = workbook.createCellStyle();
				style.setWrapText(true);

				Row row = workbook.getSheet("AmortizationSchedule").createRow(counter);

				// cell for month
				Cell cell = row.createCell(0);
				cell.setCellValue(counter);
				cell.setCellStyle(style);

				// cell for principal payment
				cell = row.createCell(1);
				cell.setCellValue(new BigDecimal(principalOnly).setScale(2, RoundingMode.CEILING).toPlainString());
				cell.setCellStyle(style);

				// cell for monthly interest payment
				cell = row.createCell(2);
				cell.setCellValue(monthlyInterest.toPlainString());
				cell.setCellStyle(style);

				// cell for remaining principal
				cell = row.createCell(3);
				cell.setCellValue(new BigDecimal(principalAmt).setScale(2, RoundingMode.CEILING).toPlainString());
				cell.setCellStyle(style);

				// increment counter for loop and month column in spreadsheet
				counter++;
			}

			// Create output file
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

			// Write workbook contents to output file
			FileOutputStream os = new FileOutputStream(fileLocation);
			workbook.write(os);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create Excel spreadsheet to contain data.
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Workbook createExcelDoc() throws IOException {
		// create workbook
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("AmortizationSchedule");
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);

		Row header = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Month");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Principal Payment");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(2);
		headerCell.setCellValue("Interest Payment");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(3);
		headerCell.setCellValue("Remaining Principal");
		headerCell.setCellStyle(headerStyle);

		return workbook;
	}

}
