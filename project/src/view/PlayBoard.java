package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import client.Client;
import enemy.Enemy;

public class PlayBoard extends Observable implements Gui, ActionListener {
	private static final String PROGRAM_TITLE = "Word Attack";
	private static final String PROGRAM_VERSION = "0.5";

	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;

	private JFrame frame;
	private JPanel panel;
	private JPanel wordPanel;
	private JTextField playerInputField;
	private JButton sendButton;

	private HashMap<Enemy, JLabel> enemies;
	private Client client;

	public PlayBoard(Client client) {
		enemies = new HashMap<Enemy, JLabel>();
		this.client = client;

		// Create components.
		playerInputField = new JTextField(20);
		playerInputField.addActionListener(this);
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);

		// Create word panel.
		wordPanel = new JPanel();
		wordPanel.setLayout(null);
		wordPanel.setBorder(new LineBorder(Color.BLACK, 1));
		wordPanel.add(playerInputField);
		wordPanel.add(sendButton);

		// Create player input panel.
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBorder(new LineBorder(Color.BLACK, 1));
		inputPanel.add(playerInputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		// Main panel
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(wordPanel, BorderLayout.CENTER);
		panel.add(inputPanel, BorderLayout.SOUTH);

		// Create frame.
		frame = new JFrame(PROGRAM_TITLE + " v" + PROGRAM_VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		frame.setResizable(false);
		frame.addWindowListener(new WindowHandler());
		frame.setVisible(true);
	}

	public void showMessage(String text) {
		JOptionPane.showMessageDialog(panel, text);
	}

	public String getPlayerInput() {
		return playerInputField.getText();
	}

	public void moveEnemies() {
		Iterator<Enemy> itr = enemies.keySet().iterator();
		while (itr.hasNext()) {
			Enemy enemy = itr.next();

			enemy.move();
			JLabel label = enemies.get(enemy);
			label.setBounds(enemy.getXPos(), enemy.getYPos(), Enemy.getWordWidth(enemy.getWord()), 20);
		}

		panel.revalidate();
	}

	public void addEnemy(Enemy enemy) {
		if (!enemies.containsKey(enemy)) {
			JLabel label = new JLabel(enemy.getWord());
			label.setBounds(enemy.getXPos(), enemy.getYPos(), Enemy.getWordWidth(enemy.getWord()), 20);
			enemies.put(enemy, label);
			wordPanel.add(label);
			panel.revalidate();
		}
	}

	public void removeEnemy(Enemy enemy) {
		if (enemies.containsKey(enemy)) {
			panel.remove(enemies.get(enemy));
			enemies.remove(enemy);
			panel.revalidate();
		}
	}

	/**
	 * <p>
	 * <code>public void actionPerformed(ActionEvent event)</code>
	 * </p>
	 * <p>
	 * Event handler method for PlayBoard.
	 * </p>
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == playerInputField) {
			actionPerformed(new ActionEvent(sendButton, ActionEvent.ACTION_PERFORMED, null));
		}
		if (event.getSource() == sendButton) {
			setChanged();
			notifyObservers();

			playerInputField.setText("");
			playerInputField.requestFocusInWindow();
		}
	}

	private class WindowHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			try {
				client.disconnect();
			} catch (IOException e1) {
				System.err.println("Could not disconnect from server!");
			}
			System.exit(0);
		}
	}
}
