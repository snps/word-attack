package view;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Gui {
	private static final String PROGRAM_TITLE = "Word Attack";
	private static final String PROGRAM_VERSION = "0.1";

	private JFrame frame;

	public Gui() {
		// Create components.
		JLabel label1 = new JLabel("Word1");
		JTextField field = new JTextField(20);
		JButton button1 = new JButton("Send");
		
		// Define positions.
		label1.setBounds(100, 100, 100, 50);
		field.setBounds(75, 100, 200, 25);
		button1.setBounds(40, 200, 75, 25);
		
		// Create panel.
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(label1);
		panel.add(field);
		panel.add(button1);
		
		// Create frame.
		frame = new JFrame(PROGRAM_TITLE + " v" + PROGRAM_VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	// TODO

	public static void main(String[] args) {
		Gui gui = new Gui();
	}
}
