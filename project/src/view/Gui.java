package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Gui extends Observable {
	private static final String PROGRAM_TITLE = "Word Attack";
	private static final String PROGRAM_VERSION = "0.1";

	private JFrame frame;
	private JTextField playerInputField;
	private JButton sendButton;

	public Gui() {
		// Create components.
		JLabel word1 = new JLabel("Word1");
		playerInputField = new JTextField(20);
		sendButton = new JButton("Send");

		// Define positions using: setBounds(x, y, width, height).
		word1.setBounds(0, -20, 100, 50);

		// Create word panel.
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(null);
		wordPanel.setBorder(new LineBorder(Color.BLACK, 1));
		wordPanel.add(word1);
		wordPanel.add(playerInputField);
		wordPanel.add(sendButton);

		// Create player input panel.
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBorder(new LineBorder(Color.BLACK, 1));
		inputPanel.add(playerInputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		// Main panel
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(wordPanel, BorderLayout.CENTER);
		panel.add(inputPanel, BorderLayout.SOUTH);

		// Create frame.
		frame = new JFrame(PROGRAM_TITLE + " v" + PROGRAM_VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public String getPlayerInput() {
		return playerInputField.getText();
	}

	// TODO

	public static void main(String[] args) {
		Gui gui = new Gui();
	}
}
