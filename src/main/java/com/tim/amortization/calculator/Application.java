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

public class Application {
	
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

	private static void createUI(JFrame frame) {

		System.out.println("Creating UI elements...");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel formPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		formPanel.setLayout(new GridLayout(3, 1));
		buttonPanel.setLayout(new FlowLayout());

		JTextField principal = addTextField(formPanel, "Principal Balance Amount: ", "600,000");
		JTextField interest = addTextField(formPanel, "Interest Percentage: ", "6.5");
		JTextField mortgageLength = addTextField(formPanel, "Mortgage Length (Years): ", "30");

		JButton calculateBtn = createButton("Calculate");
		JButton resetBtn = createButton("Reset");

		calculateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculationUtility.calculateAmortizationSchedule(principal, interest, mortgageLength);
//				BigDecimal monthlyInterest = CalculationUtility.calculateMonthlyInterest(principal, interest);
//				Double principalOnly = monthlyPayment.doubleValue() - monthlyInterest.doubleValue();
//				JOptionPane.showMessageDialog(frame, MessageFormat.format("Total Monthly Payment: {0}\nPrincipal Only: {1}\nInterest Payment: {2}", monthlyPayment, principalOnly, monthlyInterest));
				JOptionPane.showMessageDialog(frame, "Done");

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

	private static JButton createButton(String text) {

		JButton button = new JButton(text);

		return button;
	}

	private static JTextField addTextField(JPanel panel, String labelText, String fieldText) {
		JTextField textField = new JTextField(StringUtils.isNotEmpty(fieldText) ? fieldText : null);
		JLabel label = new JLabel(labelText);
		textField.setPreferredSize(new Dimension(200, 30));

		panel.add(label);
		panel.add(textField);

		return textField;
	}

}
