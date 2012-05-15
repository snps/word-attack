package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import server.Server;
import client.Client;

public class ConnectBox implements ActionListener {
	private static final String PROGRAM_TITLE = "Word Attack - Connect to game";

	public static final int PROGRAM_WIDTH = 300;
	public static final int PROGRAM_HEIGHT = 150;

	private JFrame frame;
	private JPanel panel;
	private JTextField nameField;
	private JButton joinButton;
	private JButton hostButton;

	public ConnectBox() {
		// Create components.
		JLabel nameLabel = new JLabel("Player name:");
		nameField = new JTextField();
		joinButton = new JButton("Join game");
		joinButton.addActionListener(this);
		hostButton = new JButton("Host game");
		hostButton.addActionListener(this);

		// Player name panel.
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		namePanel.add(nameLabel, BorderLayout.CENTER);
		namePanel.add(nameField, BorderLayout.SOUTH);

		// Button panel.
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.add(joinButton, BorderLayout.CENTER);
		buttonPanel.add(hostButton, BorderLayout.EAST);

		// Main panel.
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(namePanel, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.CENTER);

		// Create frame.
		frame = new JFrame(PROGRAM_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void showMessage(String text) {
		JOptionPane.showMessageDialog(panel, text);
	}

	private String getStringWithDialog(String dialogText) {
		JTextField textField = new JTextField();
		textField.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				event.getComponent().requestFocusInWindow();
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				return;
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				return;
			}
		});
		final JComponent[] inputs = new JComponent[] { new JLabel(dialogText), textField };
		JOptionPane.showMessageDialog(null, inputs, "Supply data", JOptionPane.PLAIN_MESSAGE);

		return textField.getText();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == joinButton) {
			if (nameField.getText().isEmpty()) {
				showMessage("You must supply a name");
				return;
			}
			
			// Get host address.
			String host = getStringWithDialog("Host address:");

			// Get port number.
			int port = 0;
			while (port == 0) {
				try {
					port = Integer.parseInt(getStringWithDialog("Host port:"));
				} catch (NumberFormatException e) {
					showMessage("Not a number");
				}
			}

			// Connect client to server.
			Client client = new Client(new PlayBoard(), nameField.getText());
			try {
				client.connect(host, port);
			} catch (IOException e) {
				showMessage("Failed to connect to server at " + host);
				return;
			}
		} else if (event.getSource() == hostButton) {
			if (nameField.getText().isEmpty()) {
				showMessage("You must supply a name");
				return;
			}
			
			// Get port number.
			int port = 0;
			while (port == 0) {
				try {
					port = Integer.parseInt(getStringWithDialog("Host port:"));
				} catch (NumberFormatException e) {
					showMessage("Not a number");
				}
			}

			// Get number of players.
			int nbrOfPlayers = 0;
			while (nbrOfPlayers == 0) {
				try {
					nbrOfPlayers = Integer.parseInt(getStringWithDialog("Number of players:"));
				} catch (NumberFormatException e) {
					showMessage("Not a number");
				}
			}

			// Create server.
			Server server;
			try {
				server = new Server(port, nbrOfPlayers);
			} catch (IOException e) {
				showMessage("Server could not listen on port " + port);
				return;
			}

			// Start server.
			server.start();

			// Wait for server side.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			// Connect client to local server.
			Client client = new Client(new PlayBoard(), nameField.getText());
			try {
				client.connect("localhost", port);
			} catch (IOException e) {
				showMessage("Failed to connect to local server");
				return;
			}
		}
	}
}
