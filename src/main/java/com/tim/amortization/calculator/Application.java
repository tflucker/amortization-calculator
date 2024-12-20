package com.tim.amortization.calculator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import com.tim.amortization.calculator.actions.CalculationUtility;
import com.tim.amortization.calculator.exception.InputValidationException;

/**
 * Main class of the application, creates GUI for user to provide information.
 * Calculation occurs in the CalculationUtility.java class.
 */
public class Application {

	/**
	 * Main method used to initiate the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setTitle("Ammortization Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createUI(frame);

		frame.setSize(500, 500);

		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Creates the GUI using Java Swing. Composed of JTextFields for inputs and
	 * buttons to calculate amortization or to clear the fields. Fields come
	 * pre-loaded with information as an example.
	 * 
	 * @param frame
	 */
	private static void createUI(JFrame frame) {

		System.out.println("Creating UI elements...");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// sub-panels used to group the text fields and buttons together
		JPanel formPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		formPanel.setLayout(new GridLayout(3, 1));
		buttonPanel.setLayout(new FlowLayout());

		JTextField principal = addTextField(formPanel, "Principal Balance Amount: ", "600,000");
		JTextField interest = addTextField(formPanel, "Interest Percentage: ", "6.5");
		JTextField mortgageLength = addTextField(formPanel, "Mortgage Length (Years): ", "30");

		JButton calculateBtn = createButton("Calculate");
		JButton resetBtn = createButton("Reset");

		// add action listeners (event handlers) for the each button click
		calculateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CalculationUtility.calculateAmortizationSchedule(principal, interest, mortgageLength);
					JOptionPane.showMessageDialog(frame, "Done");
				} catch (InputValidationException ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(),"An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		resetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				principal.setText(null);
				interest.setText(null);
				mortgageLength.setText(null);

				JOptionPane.showMessageDialog(frame, "Form cleared!");

			}
		});

		// add buttons to buttonPanel
		buttonPanel.add(calculateBtn);
		buttonPanel.add(resetBtn);

		// add sub-panels to main panel
		mainPanel.add(formPanel);
		mainPanel.add(buttonPanel);

		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Creates a new button with text provided.
	 * 
	 * @param text
	 * @return
	 */
	private static JButton createButton(String text) {

		JButton button = new JButton(text);

		return button;
	}

	/**
	 * Creates a label and a text field and adds it to the provided JPanel.
	 * 
	 * @param panel
	 * @param labelText
	 * @param fieldText
	 * @return
	 */
	private static JTextField addTextField(JPanel panel, String labelText, String fieldText) {
		JTextField textField = new JTextField(StringUtils.isNotEmpty(fieldText) ? fieldText : null);
		JLabel label = new JLabel(labelText);
		textField.setPreferredSize(new Dimension(200, 30));

		panel.add(label);
		panel.add(textField);

		return textField;
	}

}
