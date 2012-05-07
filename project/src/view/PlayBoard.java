package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import enemy.Enemy;

public class PlayBoard extends Observable implements Gui, ActionListener {
	private static final String PROGRAM_TITLE = "Word Attack";
	private static final String PROGRAM_VERSION = "0.2";

	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;

	private JFrame frame;
	private JPanel panel;
	private JPanel wordPanel;
	private JTextField playerInputField;
	private JButton sendButton;

	private List<Enemy> enemyList;
	private List<JLabel> labelList;

	public PlayBoard() {
		enemyList = new ArrayList<Enemy>();
		labelList = new ArrayList<JLabel>();

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
		frame.setVisible(true);
	}

	public synchronized void showMessage(String text) {
		JOptionPane.showMessageDialog(panel, text);
	}

	public String getPlayerInput() {
		return playerInputField.getText();
	}

	public void moveEnemies() {
		for (int i = 0, n = enemyList.size(); i < n; i++) {
			Enemy enemy = enemyList.get(i);

			enemy.move();

			if (labelList.size() <= i) {
				JLabel label = new JLabel();
				wordPanel.add(label);
				labelList.add(label);
			}

			JLabel label = labelList.get(i);
			label.setText(enemy.getWord());
			label.setBounds(enemy.getXPos(), enemy.getYPos(), enemy.getWordWidth(), 20);
		}

		panel.revalidate();
	}

	public void addEnemy(Enemy enemy) {
		enemyList.add(enemy);
	}

	public void removeEnemy(Enemy enemy) {
		enemyList.remove(enemy);
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
}
