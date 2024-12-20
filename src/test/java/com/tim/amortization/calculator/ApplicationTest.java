package com.tim.amortization.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.swing.JFrame;

import org.junit.jupiter.api.Test;

public class ApplicationTest {

	@Test
	public void createUITest() {
		JFrame frame = new JFrame();
		frame.setTitle("Amortization Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Application.createUI(frame);

		frame.setSize(500, 500);

		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(false);

		assertNotNull(frame);
		assertEquals("Amortization Calculator", frame.getTitle());
		
	}
}
