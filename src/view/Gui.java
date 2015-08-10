package view;

import java.util.Observable;

import enemy.Enemy;

/**
 * <p>
 * Abstract superclass for the <i>graphical user interface</i>. All Gui
 * implementations must inherit from this.
 * </p>
 */
public abstract class Gui extends Observable {
	/**
	 * <p>
	 * <code>public void createGui()</code>
	 * </p>
	 * <p>
	 * Creates and displays the Gui on screen.
	 * </p>
	 */
	public abstract void createGui();

	/**
	 * <p>
	 * <code>public String getPlayerInput()</code>
	 * </p>
	 * <p>
	 * Get current player input from Gui.
	 * </p>
	 * 
	 * @return the player input text as a String.
	 */
	public abstract String getPlayerInput();

	/**
	 * <p>
	 * <code>public void addPlayer(String playerName)</code>
	 * </p>
	 * <p>
	 * Adds a new player with the specified name, to keep score of to the Gui.
	 * Does nothing if the player already exists.
	 * </p>
	 * 
	 * @param playerName
	 *            the name of the player to add.
	 */
	public abstract void addPlayer(String playerName);

	/**
	 * <p>
	 * <code>public void removePlayer(String playerName)</code>
	 * </p>
	 * <p>
	 * Removes the specified player from the Gui. Does nothing if the player
	 * already exists.
	 * </p>
	 * 
	 * @param playerName
	 *            the name of the player.
	 */
	public abstract void removePlayer(String playerName);

	/**
	 * <p>
	 * <code>public boolean hasPlayer(String playerName)</code>
	 * </p>
	 * <p>
	 * Returns true if Gui has the player with the specified player name
	 * registered.
	 * </p>
	 * 
	 * @param playerName
	 *            the player name
	 * @return true if player is registered, false otherwise.
	 */
	public abstract boolean hasPlayer(String playerName);

	/**
	 * <p>
	 * <code>public void increasePlayerScore(String playerName, int amount)</code>
	 * </p>
	 * <p>
	 * Increases the score of the specified player with the specified amount of
	 * points.
	 * </p>
	 * 
	 * @param playerName
	 *            the name of the player.
	 * @param amount
	 *            the amount of points.
	 */
	public abstract void increasePlayerScore(String playerName, int amount);

	/**
	 * <p>
	 * <code>public void moveEnemies()</code>
	 * </p>
	 * <p>
	 * Move all enemies known to Gui.
	 * </p>
	 */
	public abstract void moveEnemies();

	/**
	 * <p>
	 * <code>public void addEnemy(Enemy enemy)</code>
	 * </p>
	 * Add the specified enemy to Gui.
	 * 
	 * @param enemy
	 *            the enemy to add.
	 */
	public abstract void addEnemy(Enemy enemy);

	/**
	 * <p>
	 * <code>public void removeEnemy(Enemy enemy)</code>
	 * </p>
	 * Remove the specified enemy from Gui.
	 * 
	 * @param enemy
	 *            the enemy to remove.
	 */
	public abstract void removeEnemy(Enemy enemy);

	/**
	 * <p>
	 * <code>public void showMessage(String text)</code>
	 * </p>
	 * Show a message dialog in the Gui with the specified text.
	 * 
	 * @param message
	 *            the message to show.
	 */
	public abstract void showMessage(String message);
}
