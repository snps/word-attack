package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import enemy.Enemy;

public class PlayBoard extends Gui implements ActionListener {
	private static final String PROGRAM_TITLE = "Word Attack";
	private static final String PROGRAM_VERSION = "0.9";

	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;

	private JFrame frame;
	private JPanel panel;
	private JPanel wordPanel;
	private JPanel scorePanel;
	private JTextField playerInputField;
	private JButton sendButton;

	private HashMap<Enemy, JLabel> enemies;
	private HashMap<String, PlayerScorePanel> players;

	public PlayBoard() {
		enemies = new HashMap<Enemy, JLabel>();
		players = new HashMap<String, PlayerScorePanel>();

		// Create components.
		playerInputField = new JTextField(20);
		playerInputField.addActionListener(this);
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);

		// Create word panel.
		wordPanel = new JPanel();
		wordPanel.setLayout(null);
		wordPanel.setBorder(new LineBorder(Color.BLACK, 1));

		// Create score panel.
		scorePanel = new JPanel();

		// Create player input panel.
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBorder(new LineBorder(Color.BLACK, 1));
		inputPanel.add(playerInputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);

		// Create bottom panel (score + player input panels).
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(scorePanel, BorderLayout.CENTER);
		bottomPanel.add(inputPanel, BorderLayout.SOUTH);

		// Main panel
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(wordPanel, BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
	}

	public void createGui() {
		// Create frame.
		frame = new JFrame(PROGRAM_TITLE + " v" + PROGRAM_VERSION);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.add(panel);
		frame.setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowHandler());
		frame.setVisible(true);
	}

	public void showMessage(String text) {
		JOptionPane.showMessageDialog(panel, text);
	}

	public String getPlayerInput() {
		return playerInputField.getText();
	}

	public void addPlayer(String playerName) {
		if (!players.containsKey(playerName)) {
			PlayerScorePanel playerPanel = new PlayerScorePanel(playerName);
			players.put(playerName, playerPanel);
			scorePanel.add(playerPanel);
			panel.revalidate();
		}
	}

	public void removePlayer(String playerName) {
		if (players.containsKey(playerName)) {
			PlayerScorePanel playerPanel = players.get(playerName);
			playerPanel.setVisible(false);
			scorePanel.remove(playerPanel);
			players.remove(playerName);
			panel.revalidate();
		}
	}
	
	public boolean hasPlayer(String playerName) {
		return players.containsKey(playerName);
	}

	public void increasePlayerScore(String playerName, int amount) {
		if (players.containsKey(playerName)) {
			players.get(playerName).addScore(amount);
			panel.revalidate();
		}
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
			JLabel label = new JLabel("<html><font color=red>" + enemy.getWord() + "</font></html>");
			label.setBounds(enemy.getXPos(), enemy.getYPos(), Enemy.getWordWidth(enemy.getWord()), 20);
			enemies.put(enemy, label);
			wordPanel.add(label);
			panel.revalidate();
		}
	}

	public void removeEnemy(Enemy enemy) {
		if (enemies.containsKey(enemy)) {
			JLabel label = enemies.get(enemy);
			label.setVisible(false);
			wordPanel.remove(label);
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

	private class PlayerScorePanel extends JPanel {
		private static final long serialVersionUID = -6411696585135193700L;

		private int score;
		private JLabel scoreLabel;

		public PlayerScorePanel(String playerName) {
			score = 0;
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(0, 50, 0, 50));
			JLabel playerLabel = new JLabel(playerName);
			scoreLabel = new JLabel(Integer.toString(score));

			add(playerLabel, BorderLayout.CENTER);
			add(scoreLabel, BorderLayout.SOUTH);
		}

		public void addScore(int amount) {
			score += amount;
			scoreLabel.setText(Integer.toString(score));
		}
	}

	private class WindowHandler extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent event) {
			setChanged();
			notifyObservers("disconnect");

			// Dispose frame.
			frame.dispose();
		}
	}
}
