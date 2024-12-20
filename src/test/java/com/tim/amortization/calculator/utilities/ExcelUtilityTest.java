package com.tim.amortization.calculator.utilities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tim.amortization.calculator.model.AmortizationRecord;

public class ExcelUtilityTest {
	
	@Test
	public void createExcelTest() throws IOException {
		// setup sample data set
		List<AmortizationRecord> records = new ArrayList<>();
		BigDecimal test = new BigDecimal(25.32123123123).setScale(2, RoundingMode.CEILING);
		records.add(new AmortizationRecord(1, test, test, test, test));
		
		// call method
		ExcelUtility.createExcelDoc(records);
		
		// verify that file exists
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
		
		File f = new File(fileLocation);
		assertTrue(f.isFile());
	}

}
