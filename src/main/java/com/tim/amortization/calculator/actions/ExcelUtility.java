package com.tim.amortization.calculator.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tim.amortization.calculator.model.AmortizationRecord;

/**
 * Class containing functionality to create the output Excel spreadsheet.
 */
public class ExcelUtility {

	/**
	 * Create Excel spreadsheet to contain data.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static void createExcelDoc(List<AmortizationRecord> records) throws IOException {
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

		// CellStyle for the data
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		// fill in data for each row using an AmortizationRecord from the List
		records.stream().forEach(rec -> {
			Row row = workbook.getSheet("AmortizationSchedule").createRow(rec.getMonth());

			// cell for month
			Cell cell = row.createCell(0);
			cell.setCellValue(rec.getMonth());
			cell.setCellStyle(style);

			// cell for principal payment
			cell = row.createCell(1);
			cell.setCellValue(rec.getPrincipalPaid().doubleValue());
			cell.setCellStyle(style);

			// cell for monthly interest payment
			cell = row.createCell(2);
			cell.setCellValue(rec.getInterestPaid().doubleValue());
			cell.setCellStyle(style);

			// cell for remaining principal
			cell = row.createCell(3);
			cell.setCellValue(rec.getRemainingPrincipal().doubleValue());
			cell.setCellStyle(style);
		});

		// Create output file
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

		// Write workbook contents to output file
		FileOutputStream os = new FileOutputStream(fileLocation);
		workbook.write(os);
		workbook.close();
	}
}
